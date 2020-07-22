package alberto.testrestpessoa.service;

import alberto.testrestpessoa.domain.Pessoa;
import alberto.testrestpessoa.repository.PessoaRepository;
import alberto.testrestpessoa.service.dto.PessoaDTO;
import alberto.testrestpessoa.service.mapper.PessoaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;

    public PessoaService(PessoaRepository pessoaRepository, PessoaMapper pessoaMapper) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaMapper = pessoaMapper;
    }

    public PessoaDTO save(PessoaDTO pessoaDTO) {
        Pessoa pessoa = pessoaMapper.toEntity(pessoaDTO);
        pessoa = pessoaRepository.save(pessoa);
        return pessoaMapper.toDto(pessoa);
    }

    @Transactional(readOnly = true)
    public Page<PessoaDTO> findAll(Pageable pageable) {
        return pessoaRepository.findAll(pageable)
                .map(pessoaMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PessoaDTO> findOne(Integer id) {
        return pessoaRepository.findById(id)
                .map(pessoaMapper::toDto);
    }

    public void delete(Integer id) {
        pessoaRepository.deleteById(id);
    }
}
