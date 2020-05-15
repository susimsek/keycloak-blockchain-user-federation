package org.keycloak.fabric.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import io.github.ecsoya.fabric.FabricPagination;
import io.github.ecsoya.fabric.FabricPaginationQuery;
import io.github.ecsoya.fabric.FabricQuery;
import io.github.ecsoya.fabric.bean.FabricObject;
import io.github.ecsoya.fabric.service.IFabricObjectService;
import lombok.RequiredArgsConstructor;
import org.keycloak.fabric.error.UserNotFoundException;
import org.keycloak.fabric.model.User;
import org.keycloak.fabric.model.constant.ModelConstant;
import org.keycloak.fabric.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final ObjectMapper objectMapper;
    private final IFabricObjectService fabricService;

    private final ModelMapper modelMapper;

    @Override
    public User get(String id) {
        Assert.notNull(id, "user id must not be null!");
        FabricObject fabricObject=fabricService.extGet(id, ModelConstant.USER_TYPE);
        if (fabricObject==null){
            return null;
        }
        User user = convertFabricObjectToUser(fabricObject);
        return user;
    }

    @Override
    public List<User> findAll(int firstResult, int maxResults) {

        FabricPaginationQuery fabricPaginationQuery=new FabricPaginationQuery();
        fabricPaginationQuery.setType(ModelConstant.USER_TYPE);
        if (firstResult != -1) {
            fabricPaginationQuery.setCurrentPage(firstResult);
        }
        if (maxResults != -1) {
            fabricPaginationQuery.setPageSize(maxResults);
        }

        List<FabricObject> fabricObjectList= fabricService.pagination(fabricPaginationQuery).getData();

        List<User> userList=new ArrayList<>();
        fabricObjectList.forEach(fabricObject -> userList.add(convertFabricObjectToUser(fabricObject)));
        return userList;
    }

    @Override
    public User findByUsername(String username) {
        FabricQuery fabricQuery=new FabricQuery();
        fabricQuery.setType(ModelConstant.USER_TYPE);
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        fabricQuery.equals("values", json);

        List<FabricObject> fabricObjectList=fabricService.extQuery(fabricQuery);
        if (fabricObjectList.isEmpty()){
            return null;
        }
        FabricObject fabricObject=fabricObjectList.get(0);
        User user=convertFabricObjectToUser(fabricObject);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        FabricQuery fabricQuery=new FabricQuery();
        fabricQuery.setType(ModelConstant.USER_TYPE);
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        fabricQuery.equals("values", json);

        List<FabricObject> fabricObjectList=fabricService.extQuery(fabricQuery);
        if (fabricObjectList.isEmpty()){
            return null;
        }
        FabricObject fabricObject=fabricObjectList.get(0);
        User user=convertFabricObjectToUser(fabricObject);
        return user;
    }

    @Override
    public User create(User user) {
        Assert.notNull(user, "user object must not be null!");
        FabricObject fabricObject=convertUserToFabricObject(user);
        Assert.notNull(fabricObject, "fabric object must not be null!");
        fabricService.create(fabricObject);
        return user;
    }

    @Override
    public User update(User user) {
        Assert.notNull(user, "user object must not be null!");
        FabricObject fabricObject=convertUserToFabricObject(user);
        Assert.notNull(fabricObject, "fabric object must not be null!");
        fabricService.update(fabricObject);
        return user;
    }

    @Override
    public void delete(String id) {
        Assert.notNull(id, "user id must not be null!");
        fabricService.delete(id,ModelConstant.USER_TYPE);
    }

    @Override
    public int count() {
        FabricQuery fabricQuery=new FabricQuery();
        fabricQuery.setType(ModelConstant.USER_TYPE);
        return fabricService.extCount(fabricQuery);
    }

    private FabricObject convertUserToFabricObject(User user){
        FabricObject fabricObject=new FabricObject();
        fabricObject.setId(user.getId());
        fabricObject.setType(user.getType());

        Map<String, Object> values = objectMapper.convertValue(user, new TypeReference<Map<String, Object>>() {});
        fabricObject.setValues(values);
        return fabricObject;
    }

    private User convertFabricObjectToUser(FabricObject fabricObject){
        Map<String, Object> values = fabricObject.getValues();

        User user = objectMapper.convertValue(values, User.class);
        return user;
    }
}
