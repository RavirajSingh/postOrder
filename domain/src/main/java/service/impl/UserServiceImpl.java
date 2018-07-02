package service.impl;

import lombok.AllArgsConstructor;
import manager.UserManager;
import model.User;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.stereotype.Component;
import repository.UserRepository;
import service.UserService;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import utils.RabbitMsgPublisher;

import java.text.MessageFormat;


@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static List<User> usersList = new ArrayList<>();
    private RabbitMsgPublisher rabbitMsgPublisher;
    private UserManager manager;
    private static final String EXCHANGE = "testExchange";
    private static final String ROUTING_KEY = "testQueue";
    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    @Override
    public Response create(User user) {
        LOG.info("creating user : " + user.toString());
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        try {
            manager.create(user);
            builder.status(Response.Status.CREATED);
            rabbitMsgPublisher.pushMsgToQueue(EXCHANGE, ROUTING_KEY, user);
            LOG.info("User created successfully!!");
        } catch (Exception e) {
            LOG.error("Error Occurred while creating user", e);
            builder.status(Response.Status.INTERNAL_SERVER_ERROR);
        }
        builder.entity(user);
        return builder.build();
    }

    @Override
    public Response update(Long id, User user) {
        LOG.info("Updating User : " + user.toString());
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        try {
            manager.update(id, user);
            builder.status(Response.Status.OK);
            String successMsg = MessageFormat.format("User {0} Updated Successfully ", id);
            LOG.info(successMsg);
        } catch (Exception e) {
            LOG.error("Error Occurred while Updating User", e);
            builder.status(Response.Status.INTERNAL_SERVER_ERROR);
            builder.tag(e.getMessage());
        }
        builder.entity(user);
        return builder.build();
    }

    @Override
    public Response get(Long id) {
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        try {
            User user = manager.findById(id);
            builder.entity(user);
            builder.status(Response.Status.FOUND);
            String successMsg = MessageFormat.format("User {0} Found : {1}", id, user.toString());
            LOG.info(successMsg);
        } catch (Exception e) {
            LOG.error("Error Occurred while fetching User", e);
            builder.status(Response.Status.NO_CONTENT);
            builder.tag(e.getMessage());
        }
        return builder.build();
    }

    @Override
    public Response getAll() {
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        List<User> userList = manager.findAll();
        builder.status(Response.Status.FOUND);
        builder.entity(userList);
        LOG.info("All Users Fetched from Db");
        return builder.build();
    }

    @Override
    public Response delete(Long id) {
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        try {
            manager.delete(id);
            builder.status(Response.Status.OK);
            builder.entity("User deleted Successfully");
        } catch (Exception e) {
            LOG.error("Error Occurred while deleting User", e);
            builder.status(Response.Status.INTERNAL_SERVER_ERROR);
            builder.tag(e.getMessage());
        }
        return builder.build();
    }
}
