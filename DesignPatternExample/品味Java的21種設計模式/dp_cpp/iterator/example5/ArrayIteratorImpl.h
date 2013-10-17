#pragma once

#include "Iterator.h"
#include "SalaryManager.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example5
				{

					///
					/// <summary> * ����ʵ�ַ�������ĵ����ӿ� </summary>
					/// 
					class ArrayIteratorImpl : public Iterator
					{
					///	
					///	 <summary> * ������ű������ľۺ϶��� </summary>
					///	 
					private:
						SalaryManager *aggregate;
					///	
					///	 <summary> * ������¼��ǰ��������λ������ </summary>
					///	 
						int index;

					public:
						ArrayIteratorImpl(SalaryManager *aggregate);


						virtual bool hasNext();


						virtual object *next();


						virtual void remove();
					};

				}
			}
		}
	}
}