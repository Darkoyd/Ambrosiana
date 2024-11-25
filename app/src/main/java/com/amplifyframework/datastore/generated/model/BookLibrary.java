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

/** This is an auto generated class representing the BookLibrary type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "BookLibraries", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, provider = "apiKey", operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE })
}, hasLazySupport = true)
public final class BookLibrary implements Model {
  public static final BookLibraryPath rootPath = new BookLibraryPath("root", false, null);
  public static final QueryField ID = field("BookLibrary", "id");
  public static final QueryField BOOK = field("BookLibrary", "bookId");
  public static final QueryField LIBRARY = field("BookLibrary", "libraryId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Book") @BelongsTo(targetName = "bookId", targetNames = {"bookId"}, type = Book.class) ModelReference<Book> book;
  private final @ModelField(targetType="UserLibrary") @BelongsTo(targetName = "libraryId", targetNames = {"libraryId"}, type = UserLibrary.class) ModelReference<UserLibrary> library;
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
  
  public ModelReference<UserLibrary> getLibrary() {
      return library;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private BookLibrary(String id, ModelReference<Book> book, ModelReference<UserLibrary> library) {
    this.id = id;
    this.book = book;
    this.library = library;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      BookLibrary bookLibrary = (BookLibrary) obj;
      return ObjectsCompat.equals(getId(), bookLibrary.getId()) &&
              ObjectsCompat.equals(getBook(), bookLibrary.getBook()) &&
              ObjectsCompat.equals(getLibrary(), bookLibrary.getLibrary()) &&
              ObjectsCompat.equals(getCreatedAt(), bookLibrary.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), bookLibrary.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getBook())
      .append(getLibrary())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("BookLibrary {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("book=" + String.valueOf(getBook()) + ", ")
      .append("library=" + String.valueOf(getLibrary()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
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
  public static BookLibrary justId(String id) {
    return new BookLibrary(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      book,
      library);
  }
  public interface BuildStep {
    BookLibrary build();
    BuildStep id(String id);
    BuildStep book(Book book);
    BuildStep library(UserLibrary library);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private ModelReference<Book> book;
    private ModelReference<UserLibrary> library;
    public Builder() {
      
    }
    
    private Builder(String id, ModelReference<Book> book, ModelReference<UserLibrary> library) {
      this.id = id;
      this.book = book;
      this.library = library;
    }
    
    @Override
     public BookLibrary build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new BookLibrary(
          id,
          book,
          library);
    }
    
    @Override
     public BuildStep book(Book book) {
        this.book = new LoadedModelReferenceImpl<>(book);
        return this;
    }
    
    @Override
     public BuildStep library(UserLibrary library) {
        this.library = new LoadedModelReferenceImpl<>(library);
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
    private CopyOfBuilder(String id, ModelReference<Book> book, ModelReference<UserLibrary> library) {
      super(id, book, library);
      
    }
    
    @Override
     public CopyOfBuilder book(Book book) {
      return (CopyOfBuilder) super.book(book);
    }
    
    @Override
     public CopyOfBuilder library(UserLibrary library) {
      return (CopyOfBuilder) super.library(library);
    }
  }
  

  public static class BookLibraryIdentifier extends ModelIdentifier<BookLibrary> {
    private static final long serialVersionUID = 1L;
    public BookLibraryIdentifier(String id) {
      super(id);
    }
  }
  
}
