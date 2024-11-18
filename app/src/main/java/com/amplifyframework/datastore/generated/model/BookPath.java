package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the Book type in your schema. */
public final class BookPath extends ModelPath<Book> {
  private AuthorPath author;
  private BookCategoryPath categories;
  private BookRatingPath ratings;
  private ListingPath listings;
  private BookWishlistPath wishlists;
  private BookLibraryPath libraries;
  BookPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, Book.class);
  }
  
  public synchronized AuthorPath getAuthor() {
    if (author == null) {
      author = new AuthorPath("author", false, this);
    }
    return author;
  }
  
  public synchronized BookCategoryPath getCategories() {
    if (categories == null) {
      categories = new BookCategoryPath("categories", true, this);
    }
    return categories;
  }
  
  public synchronized BookRatingPath getRatings() {
    if (ratings == null) {
      ratings = new BookRatingPath("ratings", true, this);
    }
    return ratings;
  }
  
  public synchronized ListingPath getListings() {
    if (listings == null) {
      listings = new ListingPath("listings", true, this);
    }
    return listings;
  }
  
  public synchronized BookWishlistPath getWishlists() {
    if (wishlists == null) {
      wishlists = new BookWishlistPath("wishlists", true, this);
    }
    return wishlists;
  }
  
  public synchronized BookLibraryPath getLibraries() {
    if (libraries == null) {
      libraries = new BookLibraryPath("libraries", true, this);
    }
    return libraries;
  }
}
