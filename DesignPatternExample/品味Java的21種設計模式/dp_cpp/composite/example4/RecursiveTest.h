#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace composite
			{
				namespace example4
				{

					class RecursiveTest
					{
					///	
					///	 <summary> * ʾ��ݹ��㷨����׳ˡ�����ֻ�Ǽ򵥵�ʵ�֣�ֻ��ʵ������ֵ��С�Ľ׳ˣ�
					///	 * �������ݱȽϴ�Ľ׳ˣ�������100�Ľ׳�Ӧ�ò���java.math.BigDecimal
					///	 * ����java.math.BigInteger </summary>
					///	 * <param name="a"> ��׳˵���ֵ </param>
					///	 * <returns> ����ֵ�Ľ׳�ֵ </returns>
					///	 
					public:
						virtual int recursive(int a);

						static void main(std::string args[]);
					};

				}
			}
		}
	}
}