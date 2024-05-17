package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  private final PostController controller;
  private static final String API_POSTS_ENDPOINT = "/api/posts";
  private static final String NOT_FOUND_MESSAGE = "Not Found";
  private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";
  private static final String METHOD_NOT_ALLOWED_MESSAGE = "Method Not Allowed";

  public MainServlet() {
    final PostRepository repository = new PostRepository();
    final PostService service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      final String path = req.getRequestURI();
      final String method = req.getMethod();

      switch (method) {
        case "GET":
          handleGetRequest(path, resp);
          break;
        case "POST":
          handlePostRequest(path, req, resp);
          break;
        case "DELETE":
          handleDeleteRequest(path, resp);
          break;
        default:
          resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
          resp.getWriter().print(METHOD_NOT_ALLOWED_MESSAGE);
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      resp.getWriter().print(INTERNAL_SERVER_ERROR_MESSAGE);
    }
  }

  private void handleGetRequest(String path, HttpServletResponse resp) throws IOException {
    if (path.equals(API_POSTS_ENDPOINT)) {
      controller.getAll(resp);
    } else if (path.matches(API_POSTS_ENDPOINT + "/\\d+")) {
      final long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
      controller.getById(id, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      resp.getWriter().print(NOT_FOUND_MESSAGE);
    }
  }

  private void handlePostRequest(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (path.equals(API_POSTS_ENDPOINT)) {
      controller.save(req.getReader(), resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      resp.getWriter().print(NOT_FOUND_MESSAGE);
    }
  }

  private void handleDeleteRequest(String path, HttpServletResponse resp) throws IOException {
    if (path.matches(API_POSTS_ENDPOINT + "/\\d+")) {
      final long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
      controller.removeById(id, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      resp.getWriter().print(NOT_FOUND_MESSAGE);
    }
  }
}

