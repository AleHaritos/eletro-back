package eletro.service;

import eletro.domain.Usuario;
import eletro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;


    public Usuario salvarUsuario(Usuario user) {
        user.setSenha(this.HashSenha(user.getSenha()));
        return this.usuarioRepository.save(user);
    }

    public Usuario getByEmailAndSenha(Usuario user) {
        user.setSenha(this.HashSenha(user.getSenha()));
        return this.usuarioRepository.findByEmailAndSenha(user.getEmail(), user.getSenha());
    }

    public Boolean getAdmByEmail(String email) {
       return this.usuarioRepository.findUsuarioByEmail(email);
    }

    private String HashSenha(String senha) {

        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte messageDigest[] = new byte[0];

        try {
            messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", 0xFF & b));
            }
           return hexString.toString();

    }

}
