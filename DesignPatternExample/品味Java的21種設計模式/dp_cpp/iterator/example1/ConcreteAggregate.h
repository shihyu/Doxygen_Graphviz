#pragma once

#include "Aggregate.h"
#include "Iterator.h"
#include "ConcreteIterator.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example1
				{

					///
					/// <summary> * ����ľۺ϶���ʵ�ִ�����Ӧ����������Ĺ��� </summary>
					/// 
					class ConcreteAggregate : public Aggregate
					{
					///	
					///	 <summary> * ʾ�⣬��ʾ�ۺ϶����������� </summary>
					///	 
					private:
//ORIGINAL LINE: private String[] ss = nullptr;
//JAVA TO C++ CONVERTER WARNING: Since the array size is not known in this declaration, Java to C++ Converter has converted this array to a pointer.  You will need to call 'delete[]' where appropriate:
						std::string *ss;

					///	
					///	 <summary> * ���췽��������ۺ϶����������� </summary>
					///	 * <param name="ss"> �ۺ϶����������� </param>
					///	 
					public:
						ConcreteAggregate(std::string ss[]);

						virtual Iterator *createIterator();
					///	
					///	 <summary> * ��ȡ��������Ӧ��Ԫ�� </summary>
					///	 * <param name="index"> ���� </param>
					///	 * <returns> ��������Ӧ��Ԫ�� </returns>
					///	 
						virtual object *get(int index);
					///	
					///	 <summary> * ��ȡ�ۺ϶���Ĵ�С </summary>
					///	 * <returns> �ۺ϶���Ĵ�С </returns>
					///	 
						virtual int size();
					};


				}
			}
		}
	}
}