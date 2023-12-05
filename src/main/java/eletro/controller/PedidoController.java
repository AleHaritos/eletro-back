package eletro.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import eletro.domain.Pedido;
import eletro.domain.dto.Order;
import eletro.domain.dto.ResponsePayment;
import eletro.domain.dto.UrlDTO;
import eletro.repository.PedidoRepository;
import eletro.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoService pedidoService;

    @Value("${front.url}")
    private String url;

    public static final String SUCCESS_URL = "http://localhost:4200/success";
    public static final String CANCEL_URL = "http://localhost:4200/cancel";

    @PostMapping("/salvar")
    public ResponseEntity<Pedido> salvarPedido(@RequestBody Pedido p) {
        return ResponseEntity.ok(pedidoService.salvarPedido(p));
    }

    @GetMapping()
    public ResponseEntity<List<Pedido>> getAllPedidos(@RequestParam Date dtInicio, @RequestParam Date dtFim) {
        return ResponseEntity.ok(pedidoRepository.findPedidosByData(dtInicio, dtFim));
    }

    @GetMapping("/year")
    public ResponseEntity<List<Date>> getPedidoByYear(@RequestParam Integer year) {
        return ResponseEntity.ok(pedidoRepository.getDateYear(year));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoRepository.findById(id).get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> checkedPedido(@PathVariable Integer id, @RequestParam Boolean check) {
        Pedido p = this.pedidoRepository.getById(id);
        p.setChecked(check);
        return ResponseEntity.ok(this.pedidoRepository.save(p));
    }

    //Gerar pagamento do pedido
    @PostMapping("/pagamento")
    public ResponseEntity<UrlDTO> getPaypalPayment(@RequestBody Order order) {
        try {
            Payment payment = pedidoService.createPayment(order.getPreco(), order.getDescricao(), CANCEL_URL, SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(new UrlDTO(link.getHref()));
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Executar pagamento do pedido
    @GetMapping("/execute")
    public ResponseEntity<ResponsePayment> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId) {
        try {
            Payment payment = pedidoService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok(new ResponsePayment(payment.toJSON(), true));
            }
        } catch (PayPalRESTException e) {
            return ResponseEntity.ok(new ResponsePayment(e.toString(), false));
        }
        return ResponseEntity.ok(new ResponsePayment(null, false));
    }
}
