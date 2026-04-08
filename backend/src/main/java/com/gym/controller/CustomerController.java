package com.gym.controller;
import com.gym.model.Customer;
import com.gym.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
public class CustomerController {

    private final CustomerRepository r;
    public CustomerController(CustomerRepository r) {
        this.r = r;
    }

    @PostMapping("/customers")
    public Customer add(@RequestBody Customer c) {
        LocalDate today = LocalDate.now();
        c.setJoinDate(today);
        c.setExpiryDate(today.plusMonths(c.getPlan()));
        return r.save(c);
    }

    @GetMapping("/customers")
    public java.util.List<Customer> all() {
        return r.findAll();
    }



    @DeleteMapping("/customers/{id}")
    public void delete(@PathVariable Long id) {
        r.deleteById(id);
    }

    @PutMapping("/customers/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer c) {
        Customer old = r.findById(id).orElseThrow();
        old.setName(c.getName());
        old.setContact(c.getContact());
        old.setEmail(c.getEmail());
        old.setAge(c.getAge());
        old.setAadhar(c.getAadhar());
        old.setPlan(c.getPlan());
        old.setAmount(c.getAmount());
        old.setExpiryDate(old.getJoinDate().plusMonths(c.getPlan()));
        return r.save(old);
    }

    @GetMapping("/invoice/{id}")
    public void invoice(@PathVariable Long id, HttpServletResponse res) throws Exception {
        Customer c = r.findById(id).get();
        res.setContentType("application/pdf");
        String safeName = c.getName().replaceAll("\\s+", "_");
        res.setHeader("Content-Disposition", "attachment; filename=" + safeName + ".pdf");
        var writer = new com.itextpdf.kernel.pdf.PdfWriter(res.getOutputStream());
        var pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        var doc = new com.itextpdf.layout.Document(pdf);
        doc.add(new com.itextpdf.layout.element.Paragraph("\t\t\t\tGym Invoice"));
        doc.add(new com.itextpdf.layout.element.Paragraph("--------------------------------------------------------------------------------------------------------------------"));
        doc.add(new com.itextpdf.layout.element.Paragraph("Name: " + c.getName()));
        doc.add(new com.itextpdf.layout.element.Paragraph("Contact: " + c.getContact()));
        doc.add(new com.itextpdf.layout.element.Paragraph("Email: " + c.getEmail()));
        doc.add(new com.itextpdf.layout.element.Paragraph("Plan: " + c.getPlan()+ " months"));
        doc.add(new com.itextpdf.layout.element.Paragraph("Amount: " + c.getAmount()));
        doc.add(new com.itextpdf.layout.element.Paragraph("Join Date: " + c.getJoinDate()));
        doc.add(new com.itextpdf.layout.element.Paragraph("Expiry Date: " + c.getExpiryDate()));
        doc.close();
    }
    @GetMapping("/customers/{id}")
    public Customer getOne(@PathVariable Long id) {
        return r.findById(id).orElseThrow();
    }

}
