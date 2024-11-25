package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.ModelReference;
import com.amplifyframework.core.model.LoadedModelReferenceImpl;
import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.ModelList;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Book type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Books", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, provider = "apiKey", operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE })
}, hasLazySupport = true)
@Index(name = "undefined", fields = {"id"})
@Index(name = "booksByIsbn", fields = {"isbn"})
public final class Book implements Model {
  public static final BookPath rootPath = new BookPath("root", false, null);
  public static final QueryField ID = field("Book", "id");
  public static final QueryField TITLE = field("Book", "title");
  public static final QueryField ISBN = field("Book", "isbn");
  public static final QueryField AUTHOR = field("Book", "authorId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String", isRequired = true) String isbn;
  private final @ModelField(targetType="Author") @BelongsTo(targetName = "authorId", targetNames = {"authorId"}, type = Author.class) ModelReference<Author> author;
  private final @ModelField(targetType="BookCategory") @HasMany(associatedWith = "book", type = BookCategory.class) ModelList<BookCategory> categories = null;
  private final @ModelField(targetType="BookRating") @HasMany(associatedWith = "book", type = BookRating.class) ModelList<BookRating> ratings = null;
  private final @ModelField(targetType="Listing") @HasMany(associatedWith = "book", type = Listing.class) ModelList<Listing> listings = null;
  private final @ModelField(targetType="BookWishlist") @HasMany(associatedWith = "book", type = BookWishlist.class) ModelList<BookWishlist> wishlists = null;
  private final @ModelField(targetType="BookLibrary") @HasMany(associatedWith = "book", type = BookLibrary.class) ModelList<BookLibrary> libraries = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getIsbn() {
      return isbn;
  }
  
  public ModelReference<Author> getAuthor() {
      return author;
  }
  
  public ModelList<BookCategory> getCategories() {
      return categories;
  }
  
  public ModelList<BookRating> getRatings() {
      return ratings;
  }
  
  public ModelList<Listing> getListings() {
      return listings;
  }
  
  public ModelList<BookWishlist> getWishlists() {
      return wishlists;
  }
  
  public ModelList<BookLibrary> getLibraries() {
      return libraries;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Book(String id, String title, String isbn, ModelReference<Author> author) {
    this.id = id;
    this.title = title;
    this.isbn = isbn;
    this.author = author;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Book book = (Book) obj;
      return ObjectsCompat.equals(getId(), book.getId()) &&
              ObjectsCompat.equals(getTitle(), book.getTitle()) &&
              ObjectsCompat.equals(getIsbn(), book.getIsbn()) &&
              ObjectsCompat.equals(getAuthor(), book.getAuthor()) &&
              ObjectsCompat.equals(getCreatedAt(), book.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), book.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getIsbn())
      .append(getAuthor())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Book {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("isbn=" + String.valueOf(getIsbn()) + ", ")
      .append("author=" + String.valueOf(getAuthor()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Book justId(String id) {
    return new Book(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      isbn,
      author);
  }
  public interface TitleStep {
    IsbnStep title(String title);
  }
  

  public interface IsbnStep {
    BuildStep isbn(String isbn);
  }
  

  public interface BuildStep {
    Book build();
    BuildStep id(String id);
    BuildStep author(Author author);
  }
  

  public static class Builder implements TitleStep, IsbnStep, BuildStep {
    private String id;
    private String title;
    private String isbn;
    private ModelReference<Author> author;
    public Builder() {
      
    }
    
    private Builder(String id, String title, String isbn, ModelReference<Author> author) {
      this.id = id;
      this.title = title;
      this.isbn = isbn;
      this.author = author;
    }
    
    @Override
     public Book build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Book(
          id,
          title,
          isbn,
          author);
    }
    
    @Override
     public IsbnStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep isbn(String isbn) {
        Objects.requireNonNull(isbn);
        this.isbn = isbn;
        return this;
    }
    
    @Override
     public BuildStep author(Author author) {
        this.author = new LoadedModelReferenceImpl<>(author);
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String isbn, ModelReference<Author> author) {
      super(id, title, isbn, author);
      Objects.requireNonNull(title);
      Objects.requireNonNull(isbn);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder isbn(String isbn) {
      return (CopyOfBuilder) super.isbn(isbn);
    }
    
    @Override
     public CopyOfBuilder author(Author author) {
      return (CopyOfBuilder) super.author(author);
    }
  }
  

  public static class BookIdentifier extends ModelIdentifier<Book> {
    private static final long serialVersionUID = 1L;
    public BookIdentifier(String id) {
      super(id);
    }
  }
  
}
