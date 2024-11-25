package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.ModelReference;
import com.amplifyframework.core.model.LoadedModelReferenceImpl;
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

/** This is an auto generated class representing the BookRating type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "BookRatings", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, provider = "apiKey", operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE })
}, hasLazySupport = true)
@Index(name = "undefined", fields = {"id"})
public final class BookRating implements Model {
  public static final BookRatingPath rootPath = new BookRatingPath("root", false, null);
  public static final QueryField ID = field("BookRating", "id");
  public static final QueryField BOOK = field("BookRating", "bookId");
  public static final QueryField RATING = field("BookRating", "rating");
  public static final QueryField DESCRIPTION = field("BookRating", "description");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Book") @BelongsTo(targetName = "bookId", targetNames = {"bookId"}, type = Book.class) ModelReference<Book> book;
  private final @ModelField(targetType="Int", isRequired = true) Integer rating;
  private final @ModelField(targetType="String") String description;
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
  
  public ModelReference<Book> getBook() {
      return book;
  }
  
  public Integer getRating() {
      return rating;
  }
  
  public String getDescription() {
      return description;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private BookRating(String id, ModelReference<Book> book, Integer rating, String description) {
    this.id = id;
    this.book = book;
    this.rating = rating;
    this.description = description;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      BookRating bookRating = (BookRating) obj;
      return ObjectsCompat.equals(getId(), bookRating.getId()) &&
              ObjectsCompat.equals(getBook(), bookRating.getBook()) &&
              ObjectsCompat.equals(getRating(), bookRating.getRating()) &&
              ObjectsCompat.equals(getDescription(), bookRating.getDescription()) &&
              ObjectsCompat.equals(getCreatedAt(), bookRating.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), bookRating.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getBook())
      .append(getRating())
      .append(getDescription())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("BookRating {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("book=" + String.valueOf(getBook()) + ", ")
      .append("rating=" + String.valueOf(getRating()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static RatingStep builder() {
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
  public static BookRating justId(String id) {
    return new BookRating(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      book,
      rating,
      description);
  }
  public interface RatingStep {
    BuildStep rating(Integer rating);
  }
  

  public interface BuildStep {
    BookRating build();
    BuildStep id(String id);
    BuildStep book(Book book);
    BuildStep description(String description);
  }
  

  public static class Builder implements RatingStep, BuildStep {
    private String id;
    private Integer rating;
    private ModelReference<Book> book;
    private String description;
    public Builder() {
      
    }
    
    private Builder(String id, ModelReference<Book> book, Integer rating, String description) {
      this.id = id;
      this.book = book;
      this.rating = rating;
      this.description = description;
    }
    
    @Override
     public BookRating build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new BookRating(
          id,
          book,
          rating,
          description);
    }
    
    @Override
     public BuildStep rating(Integer rating) {
        Objects.requireNonNull(rating);
        this.rating = rating;
        return this;
    }
    
    @Override
     public BuildStep book(Book book) {
        this.book = new LoadedModelReferenceImpl<>(book);
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
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
    private CopyOfBuilder(String id, ModelReference<Book> book, Integer rating, String description) {
      super(id, book, rating, description);
      Objects.requireNonNull(rating);
    }
    
    @Override
     public CopyOfBuilder rating(Integer rating) {
      return (CopyOfBuilder) super.rating(rating);
    }
    
    @Override
     public CopyOfBuilder book(Book book) {
      return (CopyOfBuilder) super.book(book);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
  }
  

  public static class BookRatingIdentifier extends ModelIdentifier<BookRating> {
    private static final long serialVersionUID = 1L;
    public BookRatingIdentifier(String id) {
      super(id);
    }
  }
  
}
