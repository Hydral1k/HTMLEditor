/*
 * StateEnum by Jordan Tice
 * Used to represent which state the finite state machine is in while it
 * parses a buffer for tag folding
 */
package htmleditor.outline;

/**
 *
 * @author jlt8213
 */
public enum StateEnum {
    INTEXT, INBRACKET, SAVETEXT, SAVETAG, EXIT;
}
