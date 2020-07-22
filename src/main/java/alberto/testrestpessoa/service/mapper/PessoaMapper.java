package alberto.testrestpessoa.service.mapper;


import alberto.testrestpessoa.domain.Pessoa;
import alberto.testrestpessoa.service.dto.PessoaDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface PessoaMapper {

    Pessoa toEntity(PessoaDTO dto);

    PessoaDTO toDto(Pessoa entity);

    List<Pessoa> toEntity(List<PessoaDTO> dtoList);

    List<PessoaDTO> toDto(List<Pessoa> entityList);


    default Pessoa fromId(Integer id) {
        if (id == null) {
            return null;
        }
        Pessoa pessoa = new Pessoa();
        pessoa.setId(id);
        return pessoa;
    }
}
