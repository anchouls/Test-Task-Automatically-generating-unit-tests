package org.jetbrains.person;

import org.jetbrains.car.Car;
import org.jetbrains.car.ElectricCar;
import org.jetbrains.car.PetrolCar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UnitPersonTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testNoCar() {
        assertThrows(IllegalArgumentException.class, () -> new Person(19, 10.1, 46.10,
                null));
    }

    @Test
    void testYoungPerson() {
        int age = 17;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        Car mockCar = mock(PetrolCar.class);
        Person person = new Person(age, homeLocation, workLocation, mockCar);

        person.goToWork();

        assertEquals("This person is too young to drive!",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void testGoToWork() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        PetrolCar mockCar = mock(PetrolCar.class, withSettings().useConstructor(10.0, 2.0));
        Person person = new Person(age, homeLocation, workLocation, mockCar);

        when(mockCar.needsEnergy(workLocation)).thenReturn(false);
        when(mockCar.getLocation()).thenReturn(10.0);
        when(mockCar.getEnergyValue()).thenReturn(100.0);

        person.goToWork();

        verify(mockCar, times(1)).driveTo(workLocation);
        verify(mockCar, times(1)).needsEnergy(workLocation);
        assertEquals("Drive to 46.1. Current location 10.0. Energy 100.0",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void testGoToHome() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        Car mockCar = mock(PetrolCar.class, withSettings().useConstructor(10.0, 2.0));
        Person person = new Person(age, homeLocation, workLocation, mockCar);

        when(mockCar.needsEnergy(homeLocation)).thenReturn(false);
        when(mockCar.getLocation()).thenReturn(10.0);
        when(mockCar.getEnergyValue()).thenReturn(100.0);

        person.goToHome();

        verify(mockCar, times(1)).driveTo(homeLocation);
        verify(mockCar, times(1)).needsEnergy(homeLocation);
        assertEquals("Drive to 10.1. Current location 10.0. Energy 100.0",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void testAddEnergy() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        Car mockCar = mock(PetrolCar.class, withSettings().useConstructor(10.0, 2.0));
        Person person = new Person(age, homeLocation, workLocation, mockCar);

        when(mockCar.needsEnergy(workLocation)).thenReturn(true, false);
        when(mockCar.getLocation()).thenReturn(15.0);
        when(mockCar.getEnergyValue()).thenReturn(100.0);

        person.goToWork();

        assertEquals("Needs energy\n" +
                "Drive to 25.0. Current location 15.0. Energy 100.0\n" +
                "Drive to 46.1. Current location 15.0. Energy 100.0", outputStreamCaptor.toString().trim());

        verify(mockCar, times(2)).needsEnergy(workLocation);
        verify(mockCar, times(2)).driveTo(any(double.class));
        verify(mockCar, times(1)).refuel();
    }

    @Test
    void testAddEnergyElectricCar() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        Car mockCar = mock(ElectricCar.class, withSettings().useConstructor(31.0, 2.0));
        Person person = new Person(age, homeLocation, workLocation, mockCar);

        when(mockCar.needsEnergy(workLocation)).thenReturn(true, false);
        when(mockCar.getLocation()).thenReturn(10.0);
        when(mockCar.getEnergyValue()).thenReturn(100.0);

        person.goToWork();

        assertEquals("Needs energy\n" +
                "Drive to 15.0. Current location 10.0. Energy 100.0\n" +
                "Drive to 46.1. Current location 10.0. Energy 100.0", outputStreamCaptor.toString().trim());

        verify(mockCar, times(2)).needsEnergy(workLocation);
        verify(mockCar, times(2)).driveTo(any(double.class));
        verify(mockCar, times(1)).refuel();
    }

    @Test
    void testChangeCar() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 34.1;
        Car electricCar = mock(ElectricCar.class, withSettings().useConstructor(34.1, 2.0));
        Car petrolCar = mock(PetrolCar.class, withSettings().useConstructor(10.0, 2.0));
        Person person = new Person(age, homeLocation, workLocation, petrolCar);

        when(petrolCar.needsEnergy(any(double.class))).thenReturn(false);
        when(petrolCar.getLocation()).thenReturn(10.0);
        when(petrolCar.getEnergyValue()).thenReturn(100.0);

        when(electricCar.needsEnergy(any(double.class))).thenReturn(false);
        when(electricCar.getLocation()).thenReturn(46.1);
        when(electricCar.getEnergyValue()).thenReturn(100.0);

        person.goToWork();
        person.changeCar(electricCar);
        person.goToHome();

        assertEquals("Drive to 34.1. Current location 10.0. Energy 100.0\n" +
                "Drive to 10.1. Current location 46.1. Energy 100.0",
                outputStreamCaptor.toString().trim());

        verify(petrolCar, times(1)).needsEnergy(workLocation);
        verify(petrolCar, times(1)).driveTo(any(double.class));

        verify(electricCar, times(1)).needsEnergy(homeLocation);
        verify(electricCar, times(1)).driveTo(any(double.class));
    }


    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

}
