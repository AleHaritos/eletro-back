package eletro.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.stripe.model.Payout;
import eletro.domain.Pedido;
import eletro.domain.dto.Order;
import eletro.domain.dto.UrlDTO;
import eletro.repository.PedidoRepository;
import eletro.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoService pedidoService;

    public static final String SUCCESS_URL = "http://localhost:4200/success";
    public static final String CANCEL_URL = "http://localhost:4200/cancel";

    @PostMapping("/salvar")
    public ResponseEntity<Pedido> salvarPedido(@RequestBody Pedido p) throws IOException {

        return ResponseEntity.ok(pedidoRepository.save(p));
    }

//    @GetMapping()
//    public ResponseEntity<UrlDTO> getUrlPayment() throws IOException, StripeException {
//       String url = pedidoService.pagamento();
//       return ResponseEntity.ok(new UrlDTO(url));
//    }

    @PostMapping("/pagamento")
    public ResponseEntity<UrlDTO> getPaypalPayment(@RequestBody Order order) {
        try {
            Payment payment = pedidoService.createPayment(order.getPreco(), order.getMoeda(), order.getMetodo(), order.getIntent(), order.getDescricao(), CANCEL_URL, SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(new UrlDTO(link.getHref()));
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
       return null;
    }

    @GetMapping("/execute")
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId) {
        try {
            Payment payment = pedidoService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok("sucess");
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
