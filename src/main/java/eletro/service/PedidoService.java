package eletro.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Price;
//import com.stripe.model.Product;
//import com.stripe.param.PriceCreateParams;
//import com.stripe.param.ProductCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//import static spark.Spark.post;
//import static spark.Spark.port;
//import static spark.Spark.staticFiles;
//
//import com.stripe.Stripe;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//import org.springframework.web.bind.annotation.RestController;

@Service
public class PedidoService {

    @Autowired
    private APIContext apiContext;

//    public String pagamento() throws StripeException {
//        port(4242);
//
//        Stripe.apiKey = "sk_test_51NfBGSDPl39QfcZJscxQys1iJ0bYP3tt2haqaCU69dgstMvCOHSV4tVauZb3TgYf4Qceg0KrR2oC5XwGj1p5VQiT00N0Gi9f8D";
//        Product p = createProduct();
//        PriceCreateParams paramsPrice =
//                PriceCreateParams.builder()
//                        .setProduct(p.getId())
//                        .setUnitAmount(Long.valueOf((long) 3999.99))
//                        .setCurrency("brl")
//                        .build();
//        Price price = Price.create(paramsPrice);
//
//            String YOUR_DOMAIN = "http://localhost:4242";
//            SessionCreateParams params =
//                    SessionCreateParams.builder()
//                            .setMode(SessionCreateParams.Mode.PAYMENT)
//                            .setSuccessUrl(YOUR_DOMAIN + "/success.html")
//                            .setCancelUrl(YOUR_DOMAIN + "/cancel.html")
//                            .addLineItem(
//                                    SessionCreateParams.LineItem.builder()
//                                            .setQuantity(1L)
//                                            // Provide the exact Price ID (for example, pr_1234) of the product you want to sell
//                                            .setPrice(price.getId())
//                                            .build())
//                            .build();
//            Session session = Session.create(params);
//            p.setActive(false);
//
//            return session.getUrl();
//            //response.redirect(session.getUrl(), 303);
//        //    return "";
//       // });
//    }
//
//    public Product createProduct() throws StripeException {
//        ProductCreateParams params =
//                ProductCreateParams.builder()
//                        .setName("Starter Setup")
//                        .setDefaultPriceData(
//                                ProductCreateParams.DefaultPriceData.builder()
//                                        .setUnitAmount(Long.valueOf((long) 3999.99))
//                                        .setCurrency("brl")
//                                        .build()
//                        )
//                        .addExpand("default_price")
//                        .build();
//
//        Product product = Product.create(params);
//        return product;
//    }

    public Payment createPayment(
            Double total,
            String moeda,
            String metodo,
            String intent,
            String descricao,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency("BRL");
        //total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(total.toString());

        Transaction transaction = new Transaction();
        transaction.setDescription(descricao);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(metodo.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(successUrl);
        redirectUrls.setCancelUrl(cancelUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    public void paypalPayment() {

    }
}
