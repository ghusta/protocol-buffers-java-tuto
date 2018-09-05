package com.example.tutorial;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckGenerationTest {

    @Test
    public void test1() {
        Person john = Person.newBuilder()
                .setId(123)
                .setName("John")
                .setEmail("john@company.com")
                .addPhones(Person.PhoneNumber.newBuilder()
                        .setNumber("555-5432")
                        .setType(Person.PhoneType.HOME)
                        .build())
                .build();
        assertThat(john).isNotNull();
        assertThat(john.toString()).isNotEmpty();

        AddressBook addressBook = AddressBook.newBuilder()
                .build();
        assertThat(addressBook).isNotNull();
        assertThat(addressBook.getPeopleCount()).isEqualTo(0);

        addressBook = AddressBook.newBuilder()
                .addPeople(john)
                .build();
        assertThat(addressBook.getPeopleCount()).isEqualTo(1);
        System.out.println(addressBook);
    }

    @Test
    public void testWriteToFile() throws IOException {
        Person john = Person.newBuilder()
                .setId(123)
                .setName("John")
                .setEmail("john@company.com")
                .addPhones(Person.PhoneNumber.newBuilder()
                        .setNumber("555-5432")
                        .setType(Person.PhoneType.HOME)
                        .build())
                .build();
        assertThat(john).isNotNull();

        AddressBook addressBook = AddressBook.newBuilder()
                .addPeople(john)
                .build();
        assertThat(addressBook.getPeopleCount()).isEqualTo(1);

        Path dir = Paths.get("target/test-data");
        Files.createDirectories(dir);
        OutputStream outputStream = Files.newOutputStream(Paths.get(dir.toString(), "test1.bin"));
        addressBook.writeTo(outputStream);
        assertThat(Paths.get(dir.toString(), "test1.bin")).exists();
    }

    @Test
    public void testReadFile() throws IOException {
        Path testFile = Paths.get("./src/test/resources/test-data", "test2.bin");
        assertThat(testFile).exists();

        InputStream inputStream = Files.newInputStream(testFile);
        AddressBook addressBook = AddressBook.parseFrom(inputStream);
        assertThat(addressBook).isNotNull();
        assertThat(addressBook.getPeopleCount()).isEqualTo(1);
        Person person1 = addressBook.getPeople(0);
        assertThat(person1.getName()).isEqualTo("John");
    }

}
