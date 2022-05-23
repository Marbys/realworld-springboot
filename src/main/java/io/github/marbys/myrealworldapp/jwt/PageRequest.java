package io.github.marbys.myrealworldapp.jwt;

import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequest implements Pageable {
  private final int offset;
  private final int limit;
  private final Sort sort;

  public PageRequest(int offset, int limit, Sort sort) {
    this.offset = offset;
    this.limit = limit;
    this.sort = sort;
  }

  @Override
  public int getPageNumber() {
    return offset / limit;
  }

  @Override
  public int getPageSize() {
    return limit;
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @Override
  @NonNull
  public Sort getSort() {
    return sort;
  }

  @Override
  @NonNull
  public Pageable next() {
    return new PageRequest((int) getOffset() + getPageSize(), getPageSize(), getSort());
  }

  private Pageable previous() {
    return hasPrevious()
        ? new PageRequest((int) getOffset() - getPageSize(), getPageSize(), getSort())
        : this;
  }

  @Override
  @NonNull
  public Pageable previousOrFirst() {
    return hasPrevious() ? previous() : first();
  }

  @Override
  @NonNull
  public Pageable first() {
    return new PageRequest(0, getPageSize(), getSort());
  }

  @Override
  @NonNull
  public Pageable withPage(int pageNumber) {
    return new PageRequest(offset + Math.multiplyExact(pageNumber, limit), limit, sort);
  }

  @Override
  public boolean hasPrevious() {
    return offset > limit;
  }

  public static PageRequest of(int offset, int limit, Sort sort) {
    return new PageRequest(offset, limit, sort);
  }
}
