package com.yslee.demo.boot.controller.sample;

import com.yslee.demo.boot.utils.CryptoUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sample/crypto")
public class CryptoController {

//    private static final Logger logger = LoggerFactory.getLogger(CryptoController.class);

    @GetMapping("/echo")
    public ResponseEntity<String> echo(@RequestParam String message) {
        return ResponseEntity.ok("You said '" + message + "'");
    }

    @PostMapping("/encrypt-test")
    public ResponseEntity<String> postEncryptTest(@RequestBody Map<String, String> input) {
        String message = input.get("message");
        String iv = input.get("iv");
        String result = CryptoUtils.decryptFromFront(message, "mySecretKey01234", iv);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/decrypt-test")
    public ResponseEntity<String> postDecryptTest(@RequestBody Map<String, String> input) {
        String message = input.get("message");
        String iv = input.get("iv");
        String result = CryptoUtils.encryptToFront(message, "mySecretKey01234", iv);
        return ResponseEntity.ok(result);
    }

}
