package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
  private final List<Post> posts = new ArrayList<>();
  private final AtomicLong nextId = new AtomicLong(1);

  public List<Post> all() {
    return List.copyOf(posts);
  }

  public Optional<Post> getById(long id) {
    for (Post post : posts) {
      if (post.getId() == id) {
        return Optional.of(post);
      }
    }
    return Optional.empty();
  }

  public synchronized Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(nextId.getAndIncrement());
      posts.add(post);
    } else {
      for (int i = 0; i < posts.size(); i++) {
        if (posts.get(i).getId() == post.getId()) {
          posts.set(i, post);
          return post;
        }
      }
      throw new IllegalArgumentException("Post with id " + post.getId() + " not found");
    }
    return post;
  }

  public synchronized void removeById(long id) {
    posts.removeIf(post -> post.getId() == id);
  }
}