package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
  private final Map<Long, Post> postMap = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(0);

  public Map<Long, Post> all() {
    return postMap;
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(postMap.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      long id = idGenerator.incrementAndGet();
      post.setId(id);
    }
    postMap.put(post.getId(), post);
    return post;
  }

  public void removeById(long id) {
    postMap.remove(id);
  }
}