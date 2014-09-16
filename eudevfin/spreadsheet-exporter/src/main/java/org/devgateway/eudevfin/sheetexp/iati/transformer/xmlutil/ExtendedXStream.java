package org.devgateway.eudevfin.sheetexp.iati.transformer.xmlutil;

import java.io.IOException;
import java.io.NotActiveException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Map.Entry;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StatefulWriter;

public class ExtendedXStream extends XStream {

	private final Map<String, String> attributes;

	public ExtendedXStream(final Map<String,String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public ObjectOutputStream createObjectOutputStream(final HierarchicalStreamWriter writer,
			final String rootNodeName) throws IOException {
		final StatefulWriter statefulWriter = new StatefulWriter(writer);
		statefulWriter.startNode(rootNodeName, null);

		/**
		 * This attributes section differs from the original
		 */
		if ( this.attributes != null ) {
			for (final Entry<String, String> entry : this.attributes.entrySet()) {
				statefulWriter.addAttribute(entry.getKey(), entry.getValue() );
			}
		}

		return new CustomObjectOutputStream(new CustomObjectOutputStream.StreamCallback() {
			@Override
			public void writeToStream(final Object object) {
				ExtendedXStream.this.marshal(object, statefulWriter);
			}

			@Override
			public void writeFieldsToStream(final Map fields) throws NotActiveException {
				throw new NotActiveException("not in call to writeObject");
			}

			@Override
			public void defaultWriteObject() throws NotActiveException {
				throw new NotActiveException("not in call to writeObject");
			}

			@Override
			public void flush() {
				statefulWriter.flush();
			}

			@Override
			public void close() {
				if (statefulWriter.state() != StatefulWriter.STATE_CLOSED) {
					statefulWriter.endNode();
					statefulWriter.close();
				}
			}
		});
	}
}
