package com.am.accounting.service;

import com.am.accounting.model.Client;
import com.am.accounting.repository.ClientRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    private final ClientRepository clientRepository;

    public ExcelService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> importClients(MultipartFile file) throws IOException {
        List<Client> clients = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                String name = row.getCell(0).getStringCellValue();
                String contactNumber = row.getCell(1).getStringCellValue();
                String email = row.getCell(2).getStringCellValue();
                String gstNumber = row.getCell(3).getStringCellValue();
                String address = row.getCell(4).getStringCellValue();
                Client client = new Client(name, contactNumber, email, gstNumber, address);
                clients.add(clientRepository.save(client));
            }
        }
        return clients;
    }
}
