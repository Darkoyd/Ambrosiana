package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the BookWishlist type in your schema. */
public final class BookWishlistPath extends ModelPath<BookWishlist> {
  private BookPath book;
  private WishlistPath list;
  BookWishlistPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, BookWishlist.class);
  }
  
  public synchronized BookPath getBook() {
    if (book == null) {
      book = new BookPath("book", false, this);
    }
    return book;
  }
  
  public synchronized WishlistPath getList() {
    if (list == null) {
      list = new WishlistPath("list", false, this);
    }
    return list;
  }
}
