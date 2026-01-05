import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor

class FieldSkippingClassVisitor(
    apiVersion: Int,
    nextClassVisitor: ClassVisitor,
) : ClassVisitor(apiVersion, nextClassVisitor) {

    // Returning null from this method will cause the ClassVisitor to strip all fields from the class.
    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor? = null

    abstract class Factory : AsmClassVisitorFactory<Parameters> {

        private val excludedClasses
            get() = parameters.get().classes.get()

        override fun isInstrumentable(classData: ClassData): Boolean =
            classData.className in excludedClasses

        override fun createClassVisitor(
            classContext: ClassContext,
            nextClassVisitor: org.objectweb.asm.ClassVisitor,
        ): org.objectweb.asm.ClassVisitor {
            return FieldSkippingClassVisitor(
                apiVersion = instrumentationContext.apiVersion.get(),
                nextClassVisitor = nextClassVisitor,
            )
        }
    }

    abstract class Parameters : InstrumentationParameters {
        @get:Input
        abstract val classes: SetProperty<String>
    }
}