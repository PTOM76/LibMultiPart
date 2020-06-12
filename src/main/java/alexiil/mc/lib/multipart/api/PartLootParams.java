package alexiil.mc.lib.multipart.api;

import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextType;

import alexiil.mc.lib.multipart.impl.LibMultiPart;

/** Stores some {@link LootContextParameter}s for LMP. */
public final class PartLootParams {
    private PartLootParams() {}

    /** The primary {@link AbstractPart} that was broken */
    public static final LootContextParameter<BrokenPart> BROKEN_PART;

    /** Any additional {@link AbstractPart}'s that were broken because they
     * {@link AbstractPart#addRequiredPart(AbstractPart) required} the main broken part. */
    public static final LootContextParameter<BrokenPart[]> ADDITIONAL_PARTS;

    public static final LootContextType PART_TYPE;

    static {
        BROKEN_PART = new LootContextParameter<>(LibMultiPart.id("broken_part"));
        ADDITIONAL_PARTS = new LootContextParameter<>(LibMultiPart.id("additional_parts"));
        PART_TYPE = new LootContextType.Builder().build();
    }

    /** An {@link AbstractPart} that was broken.
     * <p>
     * There are two subclasses: {@link BrokenSinglePart} (for normal parts), and {@link BrokenSubPart} (for
     * {@link SubdividedPart}s). */
    public static abstract class BrokenPart {
        private BrokenPart() {
            // Private: only BrokenSinglePart and BrokenSubPart are meant to extend this
        }

        /** @return The {@link AbstractPart} associated with this. */
        public abstract AbstractPart getPart();
    }

    public static final class BrokenSinglePart extends BrokenPart {
        public final AbstractPart part;

        public BrokenSinglePart(AbstractPart part) {
            this.part = part;
        }

        @Override
        public AbstractPart getPart() {
            return part;
        }
    }

    public static final class BrokenSubPart<Sub> extends BrokenPart {
        public final SubdividedPart<Sub> mainPart;
        public final Sub subPart;

        public BrokenSubPart(SubdividedPart<Sub> mainPart, Sub subPart) {
            if (!(mainPart instanceof AbstractPart)) {
                throw new ClassCastException(
                    mainPart.getClass() + " implements " + SubdividedPart.class + " but doesn't extend "
                        + AbstractPart.class
                );
            }
            this.mainPart = mainPart;
            this.subPart = subPart;
        }

        @Override
        public AbstractPart getPart() {
            return (AbstractPart) mainPart;
        }
    }
}