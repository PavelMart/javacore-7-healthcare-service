package ru.netology.patient.service.medical;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MedicalServiceImplTest {
    static PatientInfoFileRepository patientInfoFileRepository;
    static SendAlertService sendAlertService;
    static MedicalService medicalService;
    static PatientInfo patientInfo;

    @BeforeEach
    public void initialize() {
        patientInfoFileRepository = mock(PatientInfoFileRepository.class);
        sendAlertService = mock(SendAlertServiceImpl.class);
        medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        LocalDate birthday = LocalDate.of(1996, 12, 24);
        BloodPressure normalBloodPressure = new BloodPressure(120, 80);
        BigDecimal normalTemperature = new BigDecimal("36.6");
        HealthInfo healthInfo = new HealthInfo(normalTemperature, normalBloodPressure);
        patientInfo = new PatientInfo("Pavel", "Martakov", birthday, healthInfo);
        when(patientInfoFileRepository.getById(anyString())).thenReturn(patientInfo);
    }

    @Test
    void checkHighBloodPressure() {
        BloodPressure currentBloodPressure = new BloodPressure(130, 90);
        medicalService.checkBloodPressure(anyString(), currentBloodPressure);
        verify(sendAlertService, times(1)).send(anyString());
    }

    @Test
    void checkNormalBloodPressure() {
        BloodPressure currentBloodPressure = new BloodPressure(120, 80);
        medicalService.checkBloodPressure(anyString(), currentBloodPressure);
        verify(sendAlertService, never()).send(anyString());
    }

    @Test
    void checkHighTemperature() {
        BigDecimal currentTemperature = new BigDecimal("38.5");
        medicalService.checkTemperature(anyString(), currentTemperature);
        verify(sendAlertService, times(1)).send(anyString());
    }

    @Test
    void checkNormalTemperature() {
        BigDecimal currentTemperature = new BigDecimal("36.6");
        medicalService.checkTemperature(anyString(), currentTemperature);
        verify(sendAlertService, never()).send(anyString());
    }
}