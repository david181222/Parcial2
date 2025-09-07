package parcial2.Util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OSProcess;

public class PerformanceMonitor {

    private static final Logger logger = LogManager.getLogger("performance");

    private String NombreProceso; // Nombre descriptivo del proceso monitoreado
    private String MensajeLog; // Mensaje acumulativo para el log

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
        MensajeLog = "";
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
        MensajeLog += "\n === PROCESO [ " + NombreProceso + " ] === ";
        MensajeLog += "\n - Uso Memoria Inicio : " + Math.round(bytesToMegabytes(UsoMemoriaInicial)) + " MB";
        MensajeLog += "\n - Consumo CPU Inicio : " + String.format("%.2f", ConsumoCpuInicio) + " %";
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
        MensajeLog += "\n - Uso Memoria Final : " + Math.round(bytesToMegabytes(UsoMemoriaFinal)) + " MB";
        MensajeLog += "\n - Consumo CPU Final : " + String.format("%.2f", ConsumoCpuFinal) + " %";
        MensajeLog += "\n - Incremento Memoria : " + Math.round(bytesToMegabytes(diferenciaMemoria)) + " MB";
        MensajeLog += "\n - Incremento CPU : " + String.format("%.2f", diferenciaCpu) + " %";
        MensajeLog += "\n - Tiempo Ejecucion : " + diferenciaTiempo + " ms";
        MensajeLog += "\n - Memoria Disponible JVM : " + Math.round(bytesToMegabytes(runtime.freeMemory())) + " MB";
        MensajeLog += "\n - Incremento CPU del Proceso: " + String.format("%.2f", porcentajeCpuProceso) + " %";
        MensajeLog += "\n =============================== \n";
        // Métricas adicionales del sistema
        memoriaFisicaUsada = MemoriaFisica.getTotal() - MemoriaFisica.getAvailable();
        tiempoCpuProceso = procesoFinal.getKernelTime() + procesoFinal.getUserTime();
        numeroHilos = procesoFinal.getThreadCount();
        MensajeLog += "\n - Memoria Física Total: " + Math.round(bytesToMegabytes(MemoriaFisica.getTotal())) + " MB";
        MensajeLog += "\n - Memoria Física Usada: " + Math.round(bytesToMegabytes(memoriaFisicaUsada)) + " MB";
        MensajeLog += "\n - Número de Hilos Activos: " + numeroHilos;
        MensajeLog += "\n - Tiempo de CPU Usado por el Proceso: " + tiempoCpuProceso + " ms \n\n";
        // Registrar el log final
        logger.info(MensajeLog);
    }

    private double bytesToMegabytes(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }
}
