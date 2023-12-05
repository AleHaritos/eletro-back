package eletro.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eletro.domain.Produto;
import eletro.domain.dto.Cep;
import eletro.domain.dto.DadosFrete;
import eletro.domain.dto.FreteDTO;
import eletro.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CalculoService {

    @Autowired
    ProdutoRepository repository;

    public DadosFrete calcularFrete(String cep) throws Exception {
        // Validar cep
        this.validarCEP(cep);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://www.melhorenvio.com.br/api/v2/calculator?from=12914-180&to=" + cep + "&width=12&height=4&length=17&weight=1&insurance_value=50"))
                .build();



        HttpClient cliente = HttpClient.newBuilder().build();
        try {
            var response = cliente.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<FreteDTO> freteList = mapper.readValue(response.get().body(), new TypeReference<List<FreteDTO>>(){});

            if(freteList != null && !freteList.isEmpty()) {
               FreteDTO frete = freteList.stream().filter(x -> x.getId().equals(2L) && x.getName().equals("SEDEX")).findFirst().get();
               if (frete != null) {
                   DadosFrete dados = new DadosFrete();
                   dados.setValor(frete.getPrice());
                   dados.setPrazo(frete.getDelivery_time());
                   return dados;
               }
            }


            //Versão antiga para pegar XML

//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//
//            Document xmlDocument = builder.parse(new InputSource(new StringReader(xml)));
//
//            // Pega os elementos em que o nome da tag seja "c":
//            NodeList nodesValor = xmlDocument.getElementsByTagName("Valor");
//            NodeList nodesPrazo = xmlDocument.getElementsByTagName("PrazoEntrega");
//            DadosFrete dadosFrete = new DadosFrete();
//            if (nodesValor.getLength() > 0) {
//                dadosFrete.setValor(nodesValor.item(0).getTextContent());
//                dadosFrete.setPrazo(nodesPrazo.item(0).getTextContent());
//            }
//
//            return dadosFrete;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cep calcularCEP(String cep) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://viacep.com.br/ws/"+ cep +"/json/"))
                .build();

        HttpClient cliente = HttpClient.newBuilder().build();
        try {
            var response = cliente.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            Cep cepValue = new ObjectMapper().readValue(response.get().body(), Cep.class);
            return cepValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean validarCEP(String cep) throws Exception {
        Cep cepValue = this.calcularCEP(cep);
        if(cepValue.getErro() == null) {
            return false;
        } else {
            throw new Exception("CEP está inválido");
        }
    }

    @Transactional
    public String calcularEstoque(List<Produto> produtos) throws Exception {
        boolean isValid = true;
        List<Produto> produtosData = new ArrayList<>();
        for (Produto p : produtos) {
            Produto produtoData = repository.findById(p.getId()).get();
            produtosData.add(produtoData);
            if (produtoData.getEstoque() < p.getEstoque()) {
                isValid = false;
            }
        }
        if (isValid && !produtosData.isEmpty()) {
            produtosData.forEach(x -> {
             Integer qtd = produtos.stream().filter(y -> y.getId().equals(x.getId())).findFirst().get().getEstoque();
             qtd = x.getEstoque() - qtd;
             this.repository.updateEstoque(qtd, x.getId());
            });
        } else {
            throw new Exception("Estoque indiponível");
        }

        return "Sucesso";
    }

    @Transactional
    public String rollbackEstoque(List<Produto> produtos) {
        for (Produto p : produtos) {
            repository.rollbackEstoque(p.getEstoque(), p.getId());
        }
        return "sucesso";
    }
}
