package com.example.winterhold.entity;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "Loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id; /*nn*/
    @Column(name = "CustomerNumber")
    private String customerNumber;/*20 nn*/
    @Column(name = "BookCode")
    private String bookCode;
    @Column(name = "LoanDate")
    private LocalDate loanDate;/*nn*/
    @Column(name = "DueDate")
    private LocalDate dueDate;/*nn*/
    @Column(name = "ReturnDate")
    private LocalDate returnDate;/*n*/
    @Column(name = "Note")
    private String note;/*500 n*/
    @Column(name = "Extend")
    private Integer extend;/*500 n*/
    @Column(name = "Denda")
    private Long denda;/*500 n*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerNumber",insertable = false,updatable = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookCode",insertable = false,updatable = false)
    private Book book;

    public Loan(Long id, String customerNumber, String bookCode, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate, String note, Integer extend,Long denda) {
        this.id = id;
        this.customerNumber = customerNumber;
        this.bookCode = bookCode;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.note = note;
        this.extend = extend;
        this.denda = denda;
    }
}
