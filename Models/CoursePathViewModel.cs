using System.Collections.Generic;

namespace Demilingua.Models
{
    public class CoursePathViewModel
    {
        public int IdiomaId { get; set; }
        public string IdiomaNombre { get; set; }
        public List<CoursePathItem> Items { get; set; }
    }

    public class CoursePathItem
    {
        public Curso Curso { get; set; }
        public int TestId { get; set; }
        public bool IsCompleted { get; set; }
        public bool IsUnlocked { get; set; }
    }
}
