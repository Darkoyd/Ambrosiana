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

/** This is an auto generated class representing the BookCategory type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "BookCategories", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, provider = "apiKey", operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE })
}, hasLazySupport = true)
public final class BookCategory implements Model {
  public static final BookCategoryPath rootPath = new BookCategoryPath("root", false, null);
  public static final QueryField ID = field("BookCategory", "id");
  public static final QueryField CATEGORY = field("BookCategory", "categoryId");
  public static final QueryField BOOK = field("BookCategory", "bookId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Category") @BelongsTo(targetName = "categoryId", targetNames = {"categoryId"}, type = Category.class) ModelReference<Category> category;
  private final @ModelField(targetType="Book") @BelongsTo(targetName = "bookId", targetNames = {"bookId"}, type = Book.class) ModelReference<Book> book;
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
  
  public ModelReference<Category> getCategory() {
      return category;
  }
  
  public ModelReference<Book> getBook() {
      return book;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private BookCategory(String id, ModelReference<Category> category, ModelReference<Book> book) {
    this.id = id;
    this.category = category;
    this.book = book;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      BookCategory bookCategory = (BookCategory) obj;
      return ObjectsCompat.equals(getId(), bookCategory.getId()) &&
              ObjectsCompat.equals(getCategory(), bookCategory.getCategory()) &&
              ObjectsCompat.equals(getBook(), bookCategory.getBook()) &&
              ObjectsCompat.equals(getCreatedAt(), bookCategory.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), bookCategory.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getCategory())
      .append(getBook())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("BookCategory {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("category=" + String.valueOf(getCategory()) + ", ")
      .append("book=" + String.valueOf(getBook()) + ", ")
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
  public static BookCategory justId(String id) {
    return new BookCategory(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      category,
      book);
  }
  public interface BuildStep {
    BookCategory build();
    BuildStep id(String id);
    BuildStep category(Category category);
    BuildStep book(Book book);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private ModelReference<Category> category;
    private ModelReference<Book> book;
    public Builder() {
      
    }
    
    private Builder(String id, ModelReference<Category> category, ModelReference<Book> book) {
      this.id = id;
      this.category = category;
      this.book = book;
    }
    
    @Override
     public BookCategory build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new BookCategory(
          id,
          category,
          book);
    }
    
    @Override
     public BuildStep category(Category category) {
        this.category = new LoadedModelReferenceImpl<>(category);
        return this;
    }
    
    @Override
     public BuildStep book(Book book) {
        this.book = new LoadedModelReferenceImpl<>(book);
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
    private CopyOfBuilder(String id, ModelReference<Category> category, ModelReference<Book> book) {
      super(id, category, book);
      
    }
    
    @Override
     public CopyOfBuilder category(Category category) {
      return (CopyOfBuilder) super.category(category);
    }
    
    @Override
     public CopyOfBuilder book(Book book) {
      return (CopyOfBuilder) super.book(book);
    }
  }
  

  public static class BookCategoryIdentifier extends ModelIdentifier<BookCategory> {
    private static final long serialVersionUID = 1L;
    public BookCategoryIdentifier(String id) {
      super(id);
    }
  }
  
}
