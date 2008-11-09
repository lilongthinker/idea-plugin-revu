package org.sylfra.idea.plugins.revu.model;

import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sylfra.idea.plugins.revu.business.IReviewItemListener;

import java.io.Serializable;
import java.util.*;

/**
 * @author <a href="mailto:sylvain.francois@kalistick.fr">Sylvain FRANCOIS</a>
 * @version $Id$
 */
public class Review implements Serializable
{
  private History history;
  private String title;
  private String desc;
  private boolean active;
  private ReviewReferential reviewReferential;
  private Map<VirtualFile, List<ReviewItem>> itemsByFiles;
  private final transient List<IReviewItemListener> reviewItemListeners;

  public Review()
  {
    history = new History();
    itemsByFiles = new HashMap<VirtualFile, List<ReviewItem>>();
    reviewItemListeners = new LinkedList<IReviewItemListener>();
    reviewReferential = new ReviewReferential();
  }

  @NotNull
  public History getHistory()
  {
    return history;
  }

  public void setHistory(@NotNull History history)
  {
    this.history = history;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  @NotNull
  public String getTitle()
  {
    return title;
  }

  public void setTitle(@NotNull String title)
  {
    this.title = title;
  }

  public String getDesc()
  {
    return desc;
  }

  public void setDesc(String desc)
  {
    this.desc = desc;
  }

  @NotNull
  public ReviewReferential getReviewReferential()
  {
    return reviewReferential;
  }

  public void setReviewReferential(@NotNull ReviewReferential reviewReferential)
  {
    this.reviewReferential = reviewReferential;
  }

  @NotNull
  public Map<VirtualFile, List<ReviewItem>> getItemsByFiles()
  {
    return Collections.unmodifiableMap(itemsByFiles);
  }

  public void setItems(@NotNull List<ReviewItem> items)
  {
    itemsByFiles.clear();
    for (ReviewItem item : items)
    {
      List<ReviewItem> fileItems = itemsByFiles.get(item.getFile());
      if (fileItems == null)
      {
        fileItems = new ArrayList<ReviewItem>();
        itemsByFiles.put(item.getFile(), fileItems);
      }
      fileItems.add(item);
    }
  }

  @Nullable
  public List<ReviewItem> getItems(VirtualFile file)
  {
    List<ReviewItem> fileItems = itemsByFiles.get(file);
    return (fileItems == null) ? null : Collections.unmodifiableList(fileItems);
  }

  @Nullable
  public List<ReviewItem> getItems()
  {
    List<ReviewItem> result = new ArrayList<ReviewItem>();

    for (List<ReviewItem> items : itemsByFiles.values())
    {
      for (ReviewItem item : items)
      {
        result.add(item);
      }
    }

    return result;
  }

  public void addItem(@NotNull ReviewItem item)
  {
    List<ReviewItem> fileItems = itemsByFiles.get(item.getFile());
    if (fileItems == null)
    {
      fileItems = new ArrayList<ReviewItem>();
      itemsByFiles.put(item.getFile(), fileItems);
    }
    fileItems.add(item);

    for (IReviewItemListener listener : reviewItemListeners)
    {
      listener.itemAdded(item);
    }
  }

  public void removeItem(@NotNull ReviewItem item)
  {
    List<ReviewItem> fileItems = itemsByFiles.get(item.getFile());
    if (fileItems != null)
    {
      fileItems.remove(item);
    }

    for (IReviewItemListener listener : reviewItemListeners)
    {
      listener.itemDeleted(item);
    }
  }

  public void addReviewItemListener(@NotNull IReviewItemListener listener)
  {
    reviewItemListeners.add(listener);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    Review review = (Review) o;

    if (active != review.active)
    {
      return false;
    }
    if (desc != null ? !desc.equals(review.desc) : review.desc != null)
    {
      return false;
    }
    if (history != null ? !history.equals(review.history) : review.history != null)
    {
      return false;
    }
    if (itemsByFiles != null ? !itemsByFiles.equals(review.itemsByFiles) :
      review.itemsByFiles != null)
    {
      return false;
    }
    if (reviewItemListeners != null ? !reviewItemListeners.equals(review.reviewItemListeners) :
      review.reviewItemListeners != null)
    {
      return false;
    }
    if (reviewReferential != null ? !reviewReferential.equals(review.reviewReferential) :
      review.reviewReferential != null)
    {
      return false;
    }
    if (title != null ? !title.equals(review.title) : review.title != null)
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = history != null ? history.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (desc != null ? desc.hashCode() : 0);
    result = 31 * result + (active ? 1 : 0);
    result = 31 * result + (itemsByFiles != null ? itemsByFiles.hashCode() : 0);
    result = 31 * result + (reviewItemListeners != null ? reviewItemListeners.hashCode() : 0);
    result = 31 * result + (reviewReferential != null ? reviewReferential.hashCode() : 0);
    return result;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder(this).
      append("history", history).
      append("title", title).
      append("desc", desc).
      append("active", active).
      append("itemsByFiles", itemsByFiles).
      append("reviewItemListeners", reviewItemListeners).
      append("reviewReferential", reviewReferential).
      toString();
  }
}