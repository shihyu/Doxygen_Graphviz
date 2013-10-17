#pragma once

#include "Aggregate.h"
#include "PayModel.h"
#include "Iterator.h"
#include "ArrayIteratorImpl.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example6
				{

					///
					/// <summary> * ���ͻ����չ����Ǹ���˾�Ĺ��ʹ����� </summary>
					/// 
					class SalaryManager : public Aggregate
					{
					///	
					///	 <summary> * ��������� </summary>
					///	 
					private:
//ORIGINAL LINE: private PayModel[] pms = nullptr;
//JAVA TO C++ CONVERTER WARNING: Since the array size is not known in this declaration, Java to C++ Converter has converted this array to a pointer.  You will need to call 'delete[]' where appropriate:
						PayModel *pms;
					///	
					///	 <summary> * ��ȡ�����б� </summary>
					///	 * <returns> �����б� </returns>
					///	 
					public:
						virtual PayModel *getPays();
					///	
					///	 <summary> * ���㹤�ʣ���ʵӦ���кܶ������Ϊ����ʾ�Ӽ� </summary>
					///	 
						virtual void calcSalary();

						virtual Iterator *createIterator();
						virtual object *get(int index);
						virtual int size();
					};
				}
			}
		}
	}
}