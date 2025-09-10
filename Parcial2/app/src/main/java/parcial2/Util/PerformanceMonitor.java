package parcial2.Util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OSProcess;

//Se hizo un cambio en esta clase, ya no se usa un String para el log, donde se concatena, sino un StringBuilder
// para mejorar la eficiencia en la construcción del mensaje, ya que es muy largo. Pudimos evidenciar esa mejora en
// los logs de performance, notamos que el código se ejecuta algo más rápido
public class PerformanceMonitor {

    private static final Logger logger = LogManager.getLogger("performance");

    private String NombreProceso; // Nombre descriptivo del proceso monitoreado
    private StringBuilder MensajeLog; // Changed to StringBuilder for efficiency

    private long TiempoInicio; // Tiempo de inicio del monitoreo (ms)
    private long TiempoFinalizado; // Tiempo de finalización del monitoreo (ms)

    private double ConsumoCpuInicio; // Consumo de CPU global al inicio (%)
    private double ConsumoCpuFinal; // Consumo de CPU global al final (%)

    private long UsoMemoriaInicial; // Uso de memoria de la JVM al inicio (bytes)
    private long UsoMemoriaFinal; // Uso de memoria de la JVM al final (bytes)

    // Objetos OSHI para obtener métricas del sistema
    private SystemInfo InformacionSistema; // Información del sistema usando OSHI
    private CentralProcessor Procesador; // Información del procesador
    private GlobalMemory MemoriaFisica; // Información de la memoria física
    private OperatingSystem Os; // Información del sistema operativo
    private long[] TicksIniciales; // Array de ticks iniciales del procesador

    private OSProcess procesoInicio; // Estado del proceso al inicio
    private OSProcess procesoFinal; // Estado del proceso al final

    // Constructor que inicializa los objetos OSHI y el nombre del proceso.
    public PerformanceMonitor(String nombreProceso) {
        NombreProceso = nombreProceso;
        MensajeLog = new StringBuilder(); // Initialize StringBuilder
        InformacionSistema = new SystemInfo();
        Procesador = InformacionSistema.getHardware().getProcessor();
        MemoriaFisica = InformacionSistema.getHardware().getMemory();
        Os = InformacionSistema.getOperatingSystem();
    }

    /**
     * Inicia el monitoreo, registrando métricas iniciales de memoria, CPU y
     * proceso.
     */
    public void inicio() {
        // Registrar el tiempo de inicio del monitoreo
        TiempoInicio = System.currentTimeMillis();
        // Calcular el uso de memoria inicial de la JVM
        UsoMemoriaInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // Obtener los ticks iniciales del procesador
        TicksIniciales = Procesador.getSystemCpuLoadTicks();
        // Calcular el consumo de CPU global inicial (promedio del sistema)
        ConsumoCpuInicio = Procesador.getSystemCpuLoadBetweenTicks(TicksIniciales) * 100;
        // Guardar el estado del proceso al inicio
        procesoInicio = Os.getProcess(Os.getProcessId());
        // Mensaje inicial para el log
        MensajeLog.append("\n === PROCESO [ ").append(NombreProceso).append(" ] === ");
        MensajeLog.append("\n - Uso Memoria Inicio : ").append(Math.round(bytesToMegabytes(UsoMemoriaInicial))).append(" MB");
        MensajeLog.append("\n - Consumo CPU Inicio : ").append(String.format("%.2f", ConsumoCpuInicio)).append(" %");
    }

    /**
     * Finaliza el monitoreo, registrando métricas finales y calculando diferencias.
     * Incluye el cálculo preciso del uso de CPU por el proceso monitoreado.
     */
    public void finalizado() {
        Runtime runtime = Runtime.getRuntime();
        // Variables para diferencias y métricas adicionales
        long diferenciaTiempo = 0;
        long diferenciaMemoria = 0;
        double diferenciaCpu = 0.0;
        long memoriaFisicaUsada = 0;
        long tiempoCpuProceso = 0;
        int numeroHilos = 0;
        // Registrar el tiempo de finalización del monitoreo
        TiempoFinalizado = System.currentTimeMillis();
        // Calcular el uso de memoria final de la JVM
        UsoMemoriaFinal = runtime.totalMemory() - runtime.freeMemory();
        // Calcular el consumo de CPU global final (promedio del sistema)
        ConsumoCpuFinal = Procesador.getSystemCpuLoadBetweenTicks(TicksIniciales) * 100;
        // Guardar el estado del proceso al final
        procesoFinal = Os.getProcess(Os.getProcessId());

        // === IMPLEMENTACIÓN DETALLADA DEL USO DE CPU POR EL PROCESO ===
        // Con OSHI se comparan dos "fotografías" del proceso: una al inicio y otra al
        // final.
        // El método getProcessCpuLoadBetweenTicks calcula el porcentaje de CPU
        // utilizado
        // por el proceso en ese intervalo de tiempo, sumando los ticks de todos los
        // núcleos.
        //
        // Interpretación del resultado:
        // - Un valor cercano a 100% indica que el proceso ocupó un núcleo completo
        // durante todo el intervalo.
        // - Un valor mayor a 100% indica que el proceso usó varios núcleos en paralelo.
        // - Un valor bajo indica que el proceso pasó más tiempo esperando (I/O, pausas)
        // que ejecutando cálculos.
        //
        // Nota: esta métrica complementa al tiempo de ejecución. Mientras que el tiempo
        // muestra cuánto tardó el algoritmo, este porcentaje refleja cuánto trabajo
        // real hizo la CPU durante ese período.
        double porcentajeCpuProceso = procesoFinal.getProcessCpuLoadBetweenTicks(procesoInicio) * 100.0;
        // Calcular las diferencias entre las métricas iniciales y finales
        diferenciaTiempo = TiempoFinalizado - TiempoInicio;
        diferenciaMemoria = UsoMemoriaFinal - UsoMemoriaInicial;
        diferenciaCpu = ConsumoCpuFinal - ConsumoCpuInicio;
        // Agregar métricas al log
        MensajeLog.append("\n - Uso Memoria Final : ").append(Math.round(bytesToMegabytes(UsoMemoriaFinal))).append(" MB");
        MensajeLog.append("\n - Consumo CPU Final : ").append(String.format("%.2f", ConsumoCpuFinal)).append(" %");
        MensajeLog.append("\n - Incremento Memoria : ").append(Math.round(bytesToMegabytes(diferenciaMemoria))).append(" MB");
        MensajeLog.append("\n - Incremento CPU : ").append(String.format("%.2f", diferenciaCpu)).append(" %");
        MensajeLog.append("\n - Tiempo Ejecucion : ").append(diferenciaTiempo).append(" ms");
        MensajeLog.append("\n - Memoria Disponible JVM : ").append(Math.round(bytesToMegabytes(runtime.freeMemory()))).append(" MB");
        MensajeLog.append("\n - Incremento CPU del Proceso: ").append(String.format("%.2f", porcentajeCpuProceso)).append(" %");
        MensajeLog.append("\n =============================== \n");
        // Métricas adicionales del sistema
        memoriaFisicaUsada = MemoriaFisica.getTotal() - MemoriaFisica.getAvailable();
        tiempoCpuProceso = procesoFinal.getKernelTime() + procesoFinal.getUserTime();
        numeroHilos = procesoFinal.getThreadCount();
        MensajeLog.append("\n - Memoria Física Total: ").append(Math.round(bytesToMegabytes(MemoriaFisica.getTotal()))).append(" MB");
        MensajeLog.append("\n - Memoria Física Usada: ").append(Math.round(bytesToMegabytes(memoriaFisicaUsada))).append(" MB");
        MensajeLog.append("\n - Número de Hilos Activos: ").append(numeroHilos);
        MensajeLog.append("\n - Tiempo de CPU Usado por el Proceso: ").append(tiempoCpuProceso).append(" ms \n\n");
        // Registrar el log final
        logger.info(MensajeLog.toString());
    }

    private double bytesToMegabytes(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }
}
