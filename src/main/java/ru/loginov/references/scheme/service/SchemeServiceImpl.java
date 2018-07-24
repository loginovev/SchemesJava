package ru.loginov.references.scheme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.loginov.references.scheme.dto.SchemeDTO;
import ru.loginov.references.scheme.dto.SchemeResponseDTO;
import ru.loginov.references.scheme.entity.SchemeData;
import ru.loginov.references.scheme.exceptions.CustomSchemeNotFoundException;
import ru.loginov.utils.sort_filter.SortFilterUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

/**
 * Schemes service
 */
@Service
public class SchemeServiceImpl implements SchemeService {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    /**
     * The method gets schemes for a lazy mode table
     * @param first         (int)                          - the first user (from 0)
     * @param rows          (int)                          - amount of users
     * @param sortFields    ({@literal List<SortField>})   - sorting fields
     * @param filters       ({@literal List<FilterField>}) - filtering fields
     * @return              (UserResponseDTO)              - users and total amount of users
     */
    @Override
    public SchemeResponseDTO getListScheme(int first, int rows, List<SortField> sortFields, List<FilterField> filters) {

        SchemeResponseDTO responseDTO = new SchemeResponseDTO();

        String queryString = "select s from SchemeData s ";

        if (filters.size()>0){
            queryString = queryString+"\n" + "where "+ SortFilterUtils.getQueryFilterExpression(filters,"s",SchemeData.class);
        }

        if (sortFields.size()>0){
            queryString = queryString+"\n" + "order by " + SortFilterUtils.getQuerySortExpression(sortFields,"s");
        }

        List<SchemeData> userDataList = entityManager.createQuery(queryString,SchemeData.class)
                .setFirstResult(first)
                .setMaxResults(rows)
                .getResultList();

        for (SchemeData schemeData :userDataList) {
            responseDTO.getData().add(schemeData.getSchemeStoreDTO(false));
        }

        responseDTO.setTotalRecords(count());

        return responseDTO;
    }

    @Override
    public SchemeDTO getScheme(UUID id, boolean needSchemeAsJSON) {
        SchemeData schemeData = entityManager.find(SchemeData.class,id);
        if (schemeData !=null){
            return schemeData.getSchemeStoreDTO(needSchemeAsJSON);
        }else{
            throw new CustomSchemeNotFoundException();
        }
    }

    @Override
    @Transactional
    public UUID newScheme(SchemeDTO schemeDTO) {
        SchemeData schemeData = entityManager.merge(new SchemeData(schemeDTO));
        return schemeData.getId();
    }

    @Override
    @Transactional
    public void updateScheme(SchemeDTO schemeDTO) {

        SchemeData schemeData = entityManager.find(SchemeData.class, schemeDTO.getId());
        if (schemeData != null){
            schemeData.setTitle(schemeDTO.getTitle());
            schemeData.setSchemeAsJSON(schemeDTO.getSchemeAsJSON());
            entityManager.persist(schemeData);
        }else{
            entityManager.persist(new SchemeData(schemeDTO));
        }
    }

    @Override
    @Transactional
    public void deleteScheme(UUID id) {
        SchemeData schemeData = entityManager.find(SchemeData.class,id);
        if (schemeData !=null){
            entityManager.remove(schemeData);
        }else{
            throw new CustomSchemeNotFoundException();
        }
    }

    /**
     * The method gets the total amount of users
     * @return (int)
     */
    @Override
    public int count() {
        return entityManager.createQuery("select count(*) from SchemeData",Long.class)
                .getSingleResult()
                .intValue();
    }
}
