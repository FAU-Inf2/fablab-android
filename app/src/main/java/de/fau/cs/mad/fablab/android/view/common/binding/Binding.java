package de.fau.cs.mad.fablab.android.view.common.binding;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

/***
 * Interface for Bindings
 */
public interface Binding<View, CommandParameter> {
    void bind(View view, Command<CommandParameter> command);
}
