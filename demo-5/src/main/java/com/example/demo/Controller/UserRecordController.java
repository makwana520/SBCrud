package com.example.demo.Controller;

import com.example.demo.Entity.UserRecord;
import com.example.demo.Repository.UserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRecordController {

    @Autowired
    private UserRecordRepository userRecordRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping
    public List<UserRecord> getAllUsers() {
        return userRecordRepository.findAll();
    }

    @GetMapping("/{id}")
    public UserRecord getUserById(@PathVariable int id) {
        return userRecordRepository.findById(id).orElse(null);
    }

    @PostMapping
    public UserRecord createUser(@RequestParam("fname") String fname,
                                 @RequestParam("lname") String lname,
                                 @RequestParam("gender") String gender,
                                 @RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") String dobStr,
                                 @RequestParam("email") String email,
                                 @RequestParam("hobbies") String hobbies,
                                 @RequestParam("address") String address,
                                 @RequestParam("mob_no") Long mob_no,
                                 @RequestParam("job") String job,
                                 @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {

        Date dob = null;
        try {
            dob = new SimpleDateFormat("yyyy-MM-dd").parse(dobStr);
        } catch (ParseException e) {
            e.printStackTrace();
           
        }

        String imagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            imagePath = saveUploadedFile(profileImage);
        }

        UserRecord userRecord = new UserRecord();
        userRecord.setFname(fname);
        userRecord.setLname(lname);
        userRecord.setGender(gender);
        userRecord.setDob(dob);
        userRecord.setEmail(email);
        userRecord.setHobbies(hobbies);
        userRecord.setAddress(address);
        userRecord.setMob_no(mob_no);
        userRecord.setJob(job);
        userRecord.setProfileImage(imagePath);

        return userRecordRepository.save(userRecord);
    }

    @PutMapping("/{id}")
    public UserRecord updateUser(@PathVariable int id,
                                 @RequestParam("fname") String fname,
                                 @RequestParam("lname") String lname,
                                 @RequestParam("gender") String gender,
                                 @RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") String dobStr,
                                 @RequestParam("email") String email,
                                 @RequestParam("hobbies") String hobbies,
                                 @RequestParam("address") String address,
                                 @RequestParam("mob_no") Long mob_no,
                                 @RequestParam("job") String job,
                                 @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {

        UserRecord user = userRecordRepository.findById(id).orElse(null);
        if (user != null) {
            Date dob = null;
            try {
                dob = new SimpleDateFormat("yyyy-MM-dd").parse(dobStr);
            } catch (ParseException e) {
                e.printStackTrace();
                
            }

            user.setFname(fname);
            user.setLname(lname);
            user.setGender(gender);
            user.setDob(dob);
            user.setEmail(email);
            user.setHobbies(hobbies);
            user.setAddress(address);
            user.setMob_no(mob_no);
            user.setJob(job);

            if (profileImage != null && !profileImage.isEmpty()) {
                user.setProfileImage(saveUploadedFile(profileImage));
            }

            return userRecordRepository.save(user);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userRecordRepository.deleteById(id);
    }

    private String saveUploadedFile(MultipartFile file) throws IOException {
      
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the file
        byte[] bytes = file.getBytes();
        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        Files.write(filePath, bytes);
        return filePath.toString();
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload.";
        }

        try {
            return "File uploaded successfully: " + saveUploadedFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }
}
