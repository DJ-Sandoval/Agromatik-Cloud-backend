package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.RecomendacionService;
import com.agromatik.cloud.application.port.out.RecomendacionPort;
import com.agromatik.cloud.domain.enums.Severity;
import com.agromatik.cloud.domain.enums.TipoRecomendacion;
import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.domain.model.Recomendacion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecomendacionServiceImpl implements RecomendacionService {
    private final RecomendacionPort recomendacionPort;

    @Override
    public Recomendacion generarRecomendacion(Alerta alerta) {
        Recomendacion recomendacion = construirRecomendacion(alerta);
        return recomendacionPort.guardar(recomendacion);
    }

    @Override
    public List<Recomendacion> obtenerRecomendacionesPorAlerta(Long alertaId) {
        return recomendacionPort.buscarPorAlertaId(alertaId);
    }

    @Override
    public List<Recomendacion> obtenerRecomendacionesNoImplementadas() {
        return recomendacionPort.buscarNoImplementadas();
    }

    @Override
    public void marcarComoImplementada(Long recomendacionId) {
        recomendacionPort.actualizarComoImplementada(recomendacionId);
    }

    private Recomendacion construirRecomendacion(Alerta alerta) {
        String parametro = alerta.getParametro();
        Double valor = alerta.getValorActual();
        Severity severidad = alerta.getSeveridad();
        Double umbralMin = alerta.getUmbralMin();

        return Recomendacion.builder()
                .tipo(determinarTipo(parametro))
                .parametro(parametro)
                .descripcion("Recomendación para alerta: " + alerta.getDescripcion())
                .accionRecomendada(generarAccionRecomendada(parametro, valor, severidad, umbralMin))
                .accionesAdicionales(generarAccionesAdicionales(parametro, severidad))
                .timestamp(LocalDateTime.now())
                .alerta(alerta)
                .build();
    }

    private TipoRecomendacion determinarTipo(String parametro) {
        if (parametro.contains("Temperature")) return TipoRecomendacion.TEMPERATURA;
        if (parametro.contains("Humidity")) return TipoRecomendacion.HUMEDAD;
        if (parametro.contains("SoilMoisture")) return TipoRecomendacion.RIEGO;
        if (parametro.contains("PH")) return TipoRecomendacion.PH;
        if (parametro.contains("TDS")) return TipoRecomendacion.NUTRIENTES;
        return TipoRecomendacion.SENSOR_DESCONECTADO;
    }

    private String generarAccionRecomendada(String parametro, Double valor, Severity severidad, Double umbralMin) {
        if (valor == -999.9) {
            return "Verificar conexión y estado del sensor " + parametro + ". Reemplazar si es necesario.";
        }

        Map<String, String> acciones = Map.of(
                "generalTemperature", severidad == Severity.ALTA ?
                        "Ajustar temperatura del invernadero" : "Verificar sistema de ventilación",
                "plantsTemperature", "Regular temperatura de la zona afectada",
                "generalHumidity", "Ajustar humedad ambiental",
                "plantsHumidity", "Modificar humedad en zona de plantas",
                "plantsSoilMoisture", valor < umbralMin ?
                        "Aumentar riego" : "Reducir riego",
                "waterPH", valor < 6.0 ? "Añadir solución alcalina" : "Añadir solución ácida",
                "waterTDS", "Ajustar concentración de nutrientes"
        );

        return acciones.getOrDefault(parametro, "Revisar manualmente el parámetro " + parametro);
    }

    private String generarAccionesAdicionales(String parametro, Severity severidad) {
        List<String> acciones = new ArrayList<>();

        if (severidad == Severity.CRITICA) {
            acciones.add("Notificar al equipo técnico inmediatamente");
        } else if (severidad == Severity.ALTA) {
            acciones.add("Verificar otros sensores relacionados");
            acciones.add("Revisar histórico de últimas 24 horas");
        }

        if (parametro.contains("Temperature")) {
            acciones.add("Monitorear cada 30 minutos hasta normalizar");
        } else if (parametro.contains("Humidity")) {
            acciones.add("Verificar sistema de irrigación");
        }

        return String.join("; ", acciones);
    }

    @Override
    public Page<Recomendacion> obtenerTodas(Pageable pageable) {
        return recomendacionPort.buscarTodas(pageable);
    }
}
