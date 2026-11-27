package com.duoc.saludamepo_neon.controller;

import com.duoc.saludamepo_neon.model.DetallePedido;
import com.duoc.saludamepo_neon.model.Famoso;
import com.duoc.saludamepo_neon.model.Pedido;
import com.duoc.saludamepo_neon.model.Usuario;
import com.duoc.saludamepo_neon.repository.FamosoRepository;
import com.duoc.saludamepo_neon.repository.PedidoRepository;
import com.duoc.saludamepo_neon.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "4. Pedidos y Compras", description = "Creación de pedidos desde el carrito y consulta de historial")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FamosoRepository famosoRepository;

    @GetMapping("/mis-pedidos")
    @Operation(summary = "Listar los pedidos del usuario autenticado")
    public List<Pedido> misPedidos(Authentication auth) {
        Usuario usuario = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return pedidoRepository.findByUsuarioIdOrderByFechaPedidoDesc(usuario.getId());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Listar todos los pedidos (Solo ADMIN)")
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva compra con sus dedicatorias (Usuario autenticado)")
    public ResponseEntity<?> crearPedido(@RequestBody CrearPedidoRequest request, Authentication auth) {
        Usuario usuario = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("El carrito no puede estar vacío");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEstado("PAGADO");

        List<DetallePedido> detalles = new ArrayList<>();
        int totalCalculado = 0;

        for (ItemPedidoRequest item : request.getItems()) {
            Famoso famoso = famosoRepository.findById(item.getFamosoId())
                    .orElseThrow(() -> new RuntimeException("Famoso no encontrado ID: " + item.getFamosoId()));

            int cantidad = (item.getCantidad() != null && item.getCantidad() > 0) ? item.getCantidad() : 1;
            int subtotal = famoso.getPrecio() * cantidad;
            totalCalculado += subtotal;

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setFamoso(famoso);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(famoso.getPrecio());
            detalle.setDestinatario(item.getDestinatario());
            detalle.setDedicatoria(item.getDedicatoria());
            detalles.add(detalle);
        }

        pedido.setTotal(totalCalculado);
        pedido.setDetalles(detalles);

        Pedido guardado = pedidoRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @Data
    public static class CrearPedidoRequest {
        private List<ItemPedidoRequest> items;
    }

    @Data
    public static class ItemPedidoRequest {
        private Long famosoId;
        private Integer cantidad;
        private String destinatario;
        private String dedicatoria;
    }
}