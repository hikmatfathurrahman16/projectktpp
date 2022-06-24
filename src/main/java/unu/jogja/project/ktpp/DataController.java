/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unu.jogja.project.ktpp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author HIKMATFATHURRAHMAN
 */
@Controller
public class DataController {
    
    DataJpaController djc = new DataJpaController();
    
    @RequestMapping("/ktpp")
    public String getKtp(Model model){ 
        
        List<Data> data = new ArrayList<>();
        try{
            data = djc.findDataEntities();
        }catch(Exception e){

        }
        
        model.addAttribute("data", data);
        
        return "ktpp/database";
    }
    
    @RequestMapping("/ktpp/create")
    public String create(){
        return "ktpp/create";
    }
    
    @PostMapping(value = "/ktpp/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RedirectView newData(HttpServletRequest request, Model model, @RequestParam("photo") MultipartFile file) throws ParseException, Exception{

        Data data = new Data();
        

        
        String tanggal = request.getParameter("tgllahir");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        byte[] image = file.getBytes();
        
        Long id = Long.parseLong(request.getParameter("id"));
        String agama = request.getParameter("agama");
        String alamat = request.getParameter("alamat");
        String berlaku = "Seumur Hidup";
        String jk = request.getParameter("jeniskelamin");
        String nama = request.getParameter("nama");
        String noktp = request.getParameter("noktp");
        String pekerjaan = request.getParameter("pekerjaan");
        String status = request.getParameter("status");
        String warganegara = request.getParameter("warganegara");
        
        data.setId(id);
        data.setAgama(agama);
        data.setAlamat(alamat);
        data.setBerlakuhingga(berlaku);
        data.setFoto(image);
        data.setJeniskelamin(jk);
        data.setNama(nama);
        data.setNoktp(noktp);
        data.setPekerjaan(pekerjaan);
        data.setStatus(status);
        data.setTgllahir(date);
        data.setWarganegara(warganegara);
        
        djc.create(data);
        
        return new RedirectView("/ktpp");
    }
    
    @RequestMapping("/ktpp/detail/{id}")
    public String detail(@PathVariable long id, Model model){
        
        Data data = new Data();
        
        try{
            data = djc.findData(id);
        }catch(Exception e){
            
        }
        
        String foto = "";
        if(data != null){
            foto = Base64.encodeBase64String(data.getFoto());
            model.addAttribute("foto", foto);
        }
        
        model.addAttribute("data", data);
        
        return "ktpp/detail";
    }
    
    @RequestMapping("/ktpp/edit/{id}")
    public String edit(@PathVariable long id, Model model){
        
        Data data = new Data();
        
        try{
            data = djc.findData(id);
        }catch(Exception e){
            
        }
        
        model.addAttribute("data", data);
        
        return "ktpp/editktp";
    }
    
    @PostMapping(value = "/ktpp/editdata/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RedirectView editData(@PathVariable long id, HttpServletRequest request, Model model, @RequestParam("photo") MultipartFile file) throws ParseException, Exception{
        Data data = djc.findData(id);;
        
        String tanggal = request.getParameter("tgllahir");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
        String agama = request.getParameter("agama");
        String alamat = request.getParameter("alamat");
        String berlaku = "Seumur Hidup";
        String jk = request.getParameter("jeniskelamin");
        String nama = request.getParameter("nama");
        String noktp = request.getParameter("noktp");
        String pekerjaan = request.getParameter("pekerjaan");
        String status = request.getParameter("status");
        String warganegara = request.getParameter("warganegara");
        
        data.setAgama(agama);
        data.setAlamat(alamat);
        data.setBerlakuhingga(berlaku);
        data.setJeniskelamin(jk);
        data.setNama(nama);
        data.setNoktp(noktp);
        data.setPekerjaan(pekerjaan);
        data.setStatus(status);
        data.setTgllahir(date);
        data.setWarganegara(warganegara);
        
        if(!file.isEmpty()){
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            byte[] image = file.getBytes();
            data.setFoto(image);
        }
        
        djc.edit(data);
        
        return new RedirectView("/ktpp");
    }
    
    @PostMapping(value = "/ktpp/deletedata/{id}")
    public RedirectView deleteData(@PathVariable long id) throws ParseException, Exception{
        Data data = djc.findData(id);
        try{
            if(data != null){
                djc.destroy(id);
            }
        }catch(Exception e){
            
        }
        
        return new RedirectView("/ktpp");
    }
    
    
    
}