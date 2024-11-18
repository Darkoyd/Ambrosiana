package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the BookCategory type in your schema. */
public final class BookCategoryPath extends ModelPath<BookCategory> {
  private CategoryPath category;
  private BookPath book;
  BookCategoryPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, BookCategory.class);
  }
  
  public synchronized CategoryPath getCategory() {
    if (category == null) {
      category = new CategoryPath("category", false, this);
    }
    return category;
  }
  
  public synchronized BookPath getBook() {
    if (book == null) {
      book = new BookPath("book", false, this);
    }
    return book;
  }
}
