package io.elamine.contactapi.resource;

import io.elamine.contactapi.service.ContactService;
import io.elamine.contactapi.domain.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.elamine.contactapi.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;



@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactResource {
    private final ContactService contactService;

    @CrossOrigin(origins = "https://abdelalicontact.netlify.app")
    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        //return ResponseEntity.ok().body(contactService.createContact(contact));
        return ResponseEntity.created(URI.create("/contacts/userID")).body(contactService.createContact(contact));
    }

    @CrossOrigin(origins = "https://abdelalicontact.netlify.app")
    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(contactService.getAllContacts(page, size));
    }
    @CrossOrigin(origins = "https://abdelalicontact.netlify.app")
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(contactService.getContact(id));
    }
    @CrossOrigin(origins = "https://abdelalicontact.netlify.app")
    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }


    @CrossOrigin(origins = "https://abdelalicontact.netlify.app")
    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
    @DeleteMapping("/test")
    public ResponseEntity<Void> testDelete() {
        return ResponseEntity.ok().build(); // Just responds with 200 OK
    }

    @CrossOrigin(origins = "https://abdelalicontact.netlify.app")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        try {
            contactService.deleteContact(id); // Ensure this method exists and works
            return ResponseEntity.noContent().build(); // Respond with 204 No Content
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error deleting contact: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Respond with 500
        }
    }}
