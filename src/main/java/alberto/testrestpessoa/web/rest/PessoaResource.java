package alberto.testrestpessoa.web.rest;

import alberto.testrestpessoa.service.PessoaService;
import alberto.testrestpessoa.service.dto.PessoaDTO;
import alberto.testrestpessoa.web.rest.errors.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PessoaResource {

    private final PessoaService pessoaService;

    public PessoaResource(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping("/pessoa")
    public ResponseEntity<PessoaDTO> createPessoa(@Valid @RequestBody PessoaDTO pessoaDTO) throws URISyntaxException {
        if (pessoaDTO.getId() != null) {
            throw new BadRequestException("Erro ao gravar dados de pessoa.");
        }

        PessoaDTO result = pessoaService.save(pessoaDTO);

        return ResponseEntity.created(new URI("/api/pessoa/" + result.getId())).body(result);
    }

    @PutMapping("/pessoa")
    public ResponseEntity<PessoaDTO> updatePessoa(@Valid @RequestBody PessoaDTO pessoaDTO) {
        if (pessoaDTO.getId() == null) {
            throw new BadRequestException("Erro ao gravar dados de pessoa.");
        }

        PessoaDTO result = pessoaService.save(pessoaDTO);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/pessoas")
    public ResponseEntity<List<PessoaDTO>> getAllPessoas(Pageable pageable) {
        Page<PessoaDTO> page = pessoaService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    @GetMapping("/pessoa/{id}")
    public ResponseEntity<PessoaDTO> getPessoa(@PathVariable Integer id) {
        Optional<PessoaDTO> pessoaDTO = pessoaService.findOne(id);
        return ResponseEntity.ok().body(pessoaDTO.get());
    }

    @DeleteMapping("/pessoa/{id}")
    public ResponseEntity<Void> deletePessoa(@PathVariable Integer id) {
        pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
