import React from 'react';
import {
    DatatableWrapper,
    Filter,
    Pagination,
    PaginationOptions,
    TableBody,
    TableHeader,
    BulkCheckboxControl
} from 'react-bs-datatable';
import { Col, Row, Table, Container } from 'react-bootstrap';
import Button from "react-bootstrap/Button";

const Annuary = () => {
    // Create table headers consisting of 4 columns.
    const headers= [
            {
                isFilterable: true,
                isSortable: true,
                prop: 'name',
                title: 'Name'
            },
            {
                isFilterable: false,
                isSortable: true,
                prop: 'username',
                title: 'Username'
            },
            {
                isFilterable: false,
                isSortable: true,
                prop: 'location',
                title: 'Location'
            },
            {
                prop: "button",
                title: "Actions",
                alignment: {
                    horizontal: 'center'
                },
                cell: (row) => (
                    <>
                        <a
                            onClick={() => {
                                alert(`${row.username}'s score is ${row.score}`);
                            }}
                            className="m-r-15 text-inverse-lighter">
                            <i className="fa fa-gears fa-fw fa-lg m-r-3"></i>
                        </a>
                        <a
                            onClick={() => {
                                alert(`copier ${row.score}`);
                            }}
                            className="m-r-15 text-inverse-lighter">
                            <i className="fa fa-copy fa-fw fa-lg m-r-3"></i>
                        </a>
                    </>
                )
            },
            {
                alignment: {
                    horizontal: 'center'
                },
                checkbox: {
                    className: 'table-checkbox',
                        idProp: 'name'
                },
                prop: 'checkbox'
            }
            ];

    // Randomize data of the table columns.
    // Note that the fields are all using the `prop` field of the headers.
    const body = [
            {
                username: 'john-nhoj',
                name: `John Jo`,
                location: 'Saturn',
            },
            {
                username: 'john-nhj',
                name: `John o`,
                location: 'Saturn'
            }];
    console.log(body);

    return (
        <Container>
            <DatatableWrapper
                headers={headers}
                body={body}
                checkboxProps={{
                    onCheckboxChange: function noRefCheck(){}
                }}
                paginationOptionsProps={{
                    initialState: {
                        options: [
                            5,
                            10,
                            15,
                            20
                        ],
                        rowsPerPage: 10
                    }
                }}
            >
                <Row className="mb-4">
                    <Col
                        className="d-flex flex-col justify-content-end align-items-end"
                        lg={4}
                        xs={12}
                    >
                        <Filter />
                    </Col>
                    <Col
                        className="d-flex flex-col justify-content-lg-center align-items-center justify-content-sm-start mb-2 mb-sm-0"
                        lg={4}
                        sm={6}
                        xs={12}
                    >
                        <PaginationOptions alwaysShowPagination />
                    </Col>
                    <Col
                        className="d-flex flex-col justify-content-end align-items-end"
                        lg={4}
                        sm={6}
                        xs={12}
                    >
                        <Pagination alwaysShowPagination />
                    </Col>
                    <Col
                        className="mt-2"
                        xs={12}
                    >
                        <BulkCheckboxControl />
                    </Col>
                </Row>
                <Table>
                    <TableHeader />
                    <TableBody />
                </Table>
        </DatatableWrapper>
        </Container>
    );
}

export default Annuary;
