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
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void handleGetRequest(String path, HttpServletResponse resp) throws IOException {
    if (path.equals("/api/posts")) {
      controller.getAll(resp);
    } else if (path.matches("/api/posts/\\d+")) {
      final long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
      controller.getById(id, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private void handlePostRequest(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (path.equals("/api/posts")) {
      controller.save(req.getReader(), resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private void handleDeleteRequest(String path, HttpServletResponse resp) throws IOException {
    if (path.matches("/api/posts/\\d+")) {
      final long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
      controller.removeById(id, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}

