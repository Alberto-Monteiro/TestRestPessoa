package alberto.testrestpessoa.web.rest;

import alberto.testrestpessoa.TestRestPessoaApplication;
import alberto.testrestpessoa.domain.Pessoa;
import alberto.testrestpessoa.repository.PessoaRepository;
import alberto.testrestpessoa.service.dto.PessoaDTO;
import alberto.testrestpessoa.service.mapper.PessoaMapper;
import alberto.testrestpessoa.web.rest.errors.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestRestPessoaApplication.class)
@ExtendWith({MockitoExtension.class})
@AutoConfigureMockMvc
class PessoaResourceTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "00000000000";
    private static final String UPDATED_CPF = "11111111111";

    private static final Instant DEFAULT_DATA_NASCIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_NASCIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaMapper pessoaMapper;

    @Autowired
    private MockMvc restPessoaMockMvc;

    @Autowired
    private EntityManager em;

    Pessoa pessoa;

    public static Pessoa createEntity(EntityManager em) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(DEFAULT_NOME);
        pessoa.setCpf(DEFAULT_CPF);
        pessoa.setDataNascimento(DEFAULT_DATA_NASCIMENTO);
        return pessoa;
    }

    public static Pessoa createUpdatedEntity(EntityManager em) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(UPDATED_NOME);
        pessoa.setCpf(UPDATED_CPF);
        pessoa.setDataNascimento(UPDATED_DATA_NASCIMENTO);
        return pessoa;
    }

    @BeforeEach
    public void initTest() {
        pessoa = createEntity(em);
    }

    @Test
    @Transactional
    public void createPessoa() throws Exception {
        int databaseSizeBeforeCreate = pessoaRepository.findAll().size();

        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);
        restPessoaMockMvc.perform(post("/api/pessoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(pessoaDTO)))
                .andExpect(status().isCreated());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeCreate + 1);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPessoa.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPessoa.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    public void createPessoaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pessoaRepository.findAll().size();

        pessoa.setId(1);
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        MvcResult mvcResult = restPessoaMockMvc.perform(post("/api/pessoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(pessoaDTO)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString().contains("Erro ao gravar dados de pessoa."));

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPessoaes() throws Exception {
        pessoaRepository.saveAndFlush(pessoa);

        MvcResult mvcResult = restPessoaMockMvc.perform(get("/api/pessoas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString().contains(String.valueOf(pessoa.getId())));
        assertThat(mvcResult.getResponse().getContentAsString().contains(DEFAULT_NOME));
        assertThat(mvcResult.getResponse().getContentAsString().contains(DEFAULT_CPF));
        assertThat(mvcResult.getResponse().getContentAsString().contains(String.valueOf(DEFAULT_DATA_NASCIMENTO)));
    }

    @Test
    @Transactional
    public void getPessoa() throws Exception {
        pessoaRepository.saveAndFlush(pessoa);

        MvcResult mvcResult = restPessoaMockMvc.perform(get("/api/pessoa/{id}", pessoa.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString().contains(String.valueOf(pessoa.getId())));
        assertThat(mvcResult.getResponse().getContentAsString().contains(DEFAULT_NOME));
        assertThat(mvcResult.getResponse().getContentAsString().contains(DEFAULT_CPF));
        assertThat(mvcResult.getResponse().getContentAsString().contains(String.valueOf(DEFAULT_DATA_NASCIMENTO)));
    }

    @Test
    @Transactional
    public void getNonExistingPessoa() throws Exception {
        restPessoaMockMvc.perform(get("/api/pessoaes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePessoa() throws Exception {
        pessoaRepository.saveAndFlush(pessoa);

        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        Pessoa updatedPessoa = pessoaRepository.findById(pessoa.getId()).get();
        em.detach(updatedPessoa);
        updatedPessoa.setNome(UPDATED_NOME);
        updatedPessoa.setCpf(UPDATED_CPF);
        updatedPessoa.setDataNascimento(UPDATED_DATA_NASCIMENTO);
        PessoaDTO pessoaDTO = pessoaMapper.toDto(updatedPessoa);

        restPessoaMockMvc.perform(put("/api/pessoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(pessoaDTO)))
                .andExpect(status().isOk());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPessoa.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPessoa.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    public void updateNonExistingPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        MvcResult mvcResult = restPessoaMockMvc.perform(put("/api/pessoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(pessoaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString().contains("Erro ao gravar dados de pessoa."));

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePessoa() throws Exception {
        pessoaRepository.saveAndFlush(pessoa);

        int databaseSizeBeforeDelete = pessoaRepository.findAll().size();

        restPessoaMockMvc.perform(delete("/api/pessoa/{id}", pessoa.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}