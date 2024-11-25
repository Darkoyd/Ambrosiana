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

/** This is an auto generated class representing the BookWishlist type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "BookWishlists", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, provider = "apiKey", operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE })
}, hasLazySupport = true)
public final class BookWishlist implements Model {
  public static final BookWishlistPath rootPath = new BookWishlistPath("root", false, null);
  public static final QueryField ID = field("BookWishlist", "id");
  public static final QueryField BOOK = field("BookWishlist", "bookId");
  public static final QueryField LIST = field("BookWishlist", "wishlistId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Book") @BelongsTo(targetName = "bookId", targetNames = {"bookId"}, type = Book.class) ModelReference<Book> book;
  private final @ModelField(targetType="Wishlist") @BelongsTo(targetName = "wishlistId", targetNames = {"wishlistId"}, type = Wishlist.class) ModelReference<Wishlist> list;
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
  
  public ModelReference<Wishlist> getList() {
      return list;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private BookWishlist(String id, ModelReference<Book> book, ModelReference<Wishlist> list) {
    this.id = id;
    this.book = book;
    this.list = list;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      BookWishlist bookWishlist = (BookWishlist) obj;
      return ObjectsCompat.equals(getId(), bookWishlist.getId()) &&
              ObjectsCompat.equals(getBook(), bookWishlist.getBook()) &&
              ObjectsCompat.equals(getList(), bookWishlist.getList()) &&
              ObjectsCompat.equals(getCreatedAt(), bookWishlist.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), bookWishlist.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getBook())
      .append(getList())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("BookWishlist {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("book=" + String.valueOf(getBook()) + ", ")
      .append("list=" + String.valueOf(getList()) + ", ")
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
  public static BookWishlist justId(String id) {
    return new BookWishlist(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      book,
      list);
  }
  public interface BuildStep {
    BookWishlist build();
    BuildStep id(String id);
    BuildStep book(Book book);
    BuildStep list(Wishlist list);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private ModelReference<Book> book;
    private ModelReference<Wishlist> list;
    public Builder() {
      
    }
    
    private Builder(String id, ModelReference<Book> book, ModelReference<Wishlist> list) {
      this.id = id;
      this.book = book;
      this.list = list;
    }
    
    @Override
     public BookWishlist build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new BookWishlist(
          id,
          book,
          list);
    }
    
    @Override
     public BuildStep book(Book book) {
        this.book = new LoadedModelReferenceImpl<>(book);
        return this;
    }
    
    @Override
     public BuildStep list(Wishlist list) {
        this.list = new LoadedModelReferenceImpl<>(list);
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
    private CopyOfBuilder(String id, ModelReference<Book> book, ModelReference<Wishlist> list) {
      super(id, book, list);
      
    }
    
    @Override
     public CopyOfBuilder book(Book book) {
      return (CopyOfBuilder) super.book(book);
    }
    
    @Override
     public CopyOfBuilder list(Wishlist list) {
      return (CopyOfBuilder) super.list(list);
    }
  }
  

  public static class BookWishlistIdentifier extends ModelIdentifier<BookWishlist> {
    private static final long serialVersionUID = 1L;
    public BookWishlistIdentifier(String id) {
      super(id);
    }
  }
  
}
