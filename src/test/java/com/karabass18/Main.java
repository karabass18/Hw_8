package com.karabass18;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karabass18.model.Workers;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class Main {
    ClassLoader cLoader = Main.class.getClassLoader();


    @DisplayName("Проверка zip архива")
    @Test
    void zipFileTest() throws Exception {
        Set<String> expFilesInZip = Set.of("all_countries.txt", "all_countries.csv",
                "all_countries.pdf", "all_countries.xslx");
        Set<String> actualFileInZip = new HashSet<>();
        try (
                InputStream resource = cLoader.getResourceAsStream("all_countries.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry myEntry;
            while ((myEntry = zis.getNextEntry()) != null) {
                actualFileInZip.add(myEntry.getName());
            }
            assertThat(actualFileInZip.equals(expFilesInZip));
        }
    }

    @DisplayName("Проверка PDF файла")
    @Test
    void pdfFileTest() throws Exception {
        ZipFile zfile = new ZipFile(new File("src/test/resources/all_countries.zip"));
        try (
                InputStream resource = cLoader.getResourceAsStream("all_countries.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry myEntry;
            while ((myEntry = zis.getNextEntry()) != null) {
                if (myEntry.getName().contains(".pdf")) {
                    try (InputStream inputStream = zfile.getInputStream(myEntry)) {
                        PDF content = new PDF(inputStream);
                        assertThat(content.text).contains("Azerbaijan");
                    }
                }
            }

        }
    }

    @DisplayName("Проверка CSV файла")
    @Test
    void csvFileTest() throws Exception {
        ZipFile zfile = new ZipFile(new File("src/test/resources/all_countries.zip"));
        try (
                InputStream resource = cLoader.getResourceAsStream("all_countries.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry myEntry;
            while ((myEntry = zis.getNextEntry()) != null) {
                if (myEntry.getName().contains(".csv")) {
                    try (InputStream res = zfile.getInputStream(myEntry);
                         CSVReader reader = new CSVReader(new InputStreamReader(res))) {
                        List<String[]> cont = reader.readAll();
                        assertThat(cont.get(1)[1]).contains("Albania");
                    }
                }
            }

        }
    }

    @DisplayName("Проверка XLSX файла")
    @Test
    void xslxFileTest() throws Exception {
        ZipFile zfile = new ZipFile(new File("src/test/resources/all_countries.zip"));
        try (
                InputStream resource = cLoader.getResourceAsStream("all_countries.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry myEntry;
            while ((myEntry = zis.getNextEntry()) != null) {
                if (myEntry.getName().contains(".xlsx")) {
                    try (InputStream inputStream = zfile.getInputStream(myEntry)) {
                        XLS cont = new XLS(inputStream);
                        assertThat(cont.excel.getSheetAt(0).getRow(2)
                                       .getCell(1).getStringCellValue()).contains("Algeria");
                    }
                }
            }

        }
    }

    @DisplayName("Проверка Json")
    @Test
    void jsonFileTest() throws Exception {
        ObjectMapper objectMap = new ObjectMapper();
        try (
                InputStream resource = cLoader.getResourceAsStream("workers.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            Workers workers = objectMap.readValue(reader, Workers.class);
            assertThat(workers.num).isEqualTo(3);
            assertThat(workers.title).isEqualTo("Workers");
            assertThat(workers.info.get(1).lastname).isEqualTo("Petrov");
            assertThat(workers.info.get(1).children.num).isEqualTo(1);
            assertThat(workers.info.get(2).children.childinfo.get(0).name).isEqualTo("Vanya");
            assertThat(workers.info.get(2).children.childinfo.get(2).age).isEqualTo(11);
        }
    }

}