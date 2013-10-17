#pragma once

#include "AggregationIterator.h"
#include "PayModel.h"
#include "SalaryManager.h"
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example9
				{


					///
					/// <summary> * ����ʵ�������ҳ���ʾۺ�Ԫ�صĵ����ӿ� </summary>
					/// 
					class ArrayIteratorImpl : public AggregationIterator
					{
					///	
					///	 <summary> * ������ű����������� </summary>
					///	 
					private:
//ORIGINAL LINE: private PayModel[] pms = nullptr;
//JAVA TO C++ CONVERTER WARNING: Since the array size is not known in this declaration, Java to C++ Converter has converted this array to a pointer.  You will need to call 'delete[]' where appropriate:
						PayModel *pms;
					///	
					///	 <summary> * ������¼��ǰ��������λ������ </summary>
					///	 
						int index;

					public:
						ArrayIteratorImpl(SalaryManager *aggregate);


						virtual bool hasNext();

						virtual bool hasPrevious();

						virtual Collection *getPage(int pageNum, int pageShow);
					};

				}
			}
		}
	}
}