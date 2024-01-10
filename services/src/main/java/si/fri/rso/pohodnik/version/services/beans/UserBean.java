package si.fri.rso.pohodnik.version.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.eclipse.microprofile.metrics.annotation.Timed;

import si.fri.rso.pohodnik.version.lib.User;
import si.fri.rso.pohodnik.version.models.converters.UserConverter;
import si.fri.rso.pohodnik.version.models.entities.UserEntity;


@RequestScoped
public class UserBean {

    private Logger log = Logger.getLogger(UserBean.class.getName());

    @Inject
    private EntityManager em;

    public List<User> getUser() {

        TypedQuery<UserEntity> query = em.createNamedQuery(
                "UserEntity.getAll", UserEntity.class);

        List<UserEntity> resultList = query.getResultList();

        return resultList.stream().map(UserConverter::toDto).collect(Collectors.toList());

    }
    @Timed
    public List<User> getUserFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, UserEntity.class, queryParameters).stream()
                .map(UserConverter::toDto).collect(Collectors.toList());
    }

    public User getUser(Integer id) {

        UserEntity UserEntity = em.find(UserEntity.class, id);

        if (UserEntity == null) {
            throw new NotFoundException();
        }

        User User = UserConverter.toDto(UserEntity);

        return User;
    }

    public User createUser(User User) {

        UserEntity UserEntity = UserConverter.toEntity(User);

        try {
            beginTx();
            em.persist(UserEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (UserEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return UserConverter.toDto(UserEntity);
    }

    public User putUser(Integer id, User User) {

        UserEntity c = em.find(UserEntity.class, id);

        if (c == null) {
            return null;
        }

        UserEntity updatedUserEntity = UserConverter.toEntity(User);

        try {
            beginTx();
            updatedUserEntity.setId(c.getId());
            updatedUserEntity = em.merge(updatedUserEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return UserConverter.toDto(updatedUserEntity);
    }

    public boolean deleteUser(Integer id) {

        UserEntity User = em.find(UserEntity.class, id);

        if (User != null) {
            try {
                beginTx();
                em.remove(User);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
