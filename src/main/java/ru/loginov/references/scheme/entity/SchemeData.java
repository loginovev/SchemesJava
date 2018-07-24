package ru.loginov.references.scheme.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import ru.loginov.references.scheme.dto.SchemeDTO;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "references_scheme_Scheme")
@Getter
@Setter
@NoArgsConstructor
public class SchemeData {

    @Id
    @GeneratedValue( generator = "uuid" )
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    public UUID id;

    private String title;

    @Type(type = "text")
    private String schemeAsJSON;

    public SchemeData(SchemeDTO schemeDTO) {
        this.title = schemeDTO.getTitle();
        this.schemeAsJSON = schemeDTO.getSchemeAsJSON();
    }

    public SchemeDTO getSchemeStoreDTO(boolean needSchemeAsJSON) {
        return new SchemeDTO(
                getId(),
                getTitle(),
                needSchemeAsJSON ? getSchemeAsJSON() : ""
        );
    }
}
