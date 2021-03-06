package name.abuchen.portfolio.ui.views.columns;

import java.util.Comparator;

import name.abuchen.portfolio.model.Adaptor;
import name.abuchen.portfolio.model.Attributable;
import name.abuchen.portfolio.model.AttributeType;
import name.abuchen.portfolio.model.Attributes;
import name.abuchen.portfolio.ui.Messages;
import name.abuchen.portfolio.ui.util.viewers.AttributeEditingSupport;
import name.abuchen.portfolio.ui.util.viewers.Column;
import name.abuchen.portfolio.ui.util.viewers.ColumnViewerSorter;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;

public class AttributeColumn extends Column
{
    private static final class AttributeComparator implements Comparator<Object>
    {
        private final AttributeType attribute;

        private AttributeComparator(AttributeType attribute)
        {
            this.attribute = attribute;
        }

        @Override
        public int compare(Object o1, Object o2)
        {
            Attributable a1 = Adaptor.adapt(Attributable.class, o1);
            Attributable a2 = Adaptor.adapt(Attributable.class, o2);

            if (a1 == null && a2 == null)
                return 0;
            else if (a1 == null)
                return -1;
            else if (a2 == null)
                return 1;

            Object v1 = a1.getAttributes().get(attribute);
            Object v2 = a2.getAttributes().get(attribute);

            return attribute.getComparator().compare(v1, v2);
        }
    }

    private static final class AttributeLabelProvider extends ColumnLabelProvider
    {
        private final AttributeType attribute;

        private AttributeLabelProvider(AttributeType attribute)
        {
            this.attribute = attribute;
        }

        @Override
        public String getText(Object element)
        {
            Attributable attributable = Adaptor.adapt(Attributable.class, element);
            if (attributable == null)
                return null;

            Attributes attributes = attributable.getAttributes();

            Object value = attributes.get(attribute);
            return attribute.getConverter().toString(value);
        }
    }

    public AttributeColumn(final AttributeType attribute)
    {
        super("attribute$" + attribute.getId(), attribute.getColumnLabel(), //$NON-NLS-1$
                        attribute.isNumber() ? SWT.RIGHT : SWT.LEFT, 80);

        setMenuLabel(attribute.getName());
        setGroupLabel(Messages.GroupLabelAttributes);
        setLabelProvider(new AttributeLabelProvider(attribute));
        setSorter(ColumnViewerSorter.create(new AttributeComparator(attribute)));

        new AttributeEditingSupport(attribute).attachTo(this);
    }

}
