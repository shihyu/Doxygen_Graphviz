#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace interpreter
			{
				namespace example1
				{

					using javax::xml::parsers::DocumentBuilder;
					using javax::xml::parsers::DocumentBuilderFactory;

					using namespace org::w3c::dom;
					///
					/// <summary> * ��ȡ�����ļ� </summary>
					/// 
					class ReadAppXml
					{
					///	
					///	 <summary> * ��ȡ�����ļ����� </summary>
					///	 * <param name="filePathName"> �����ļ���·�����ļ��� </param>
					///	 * <exception cref="Exception"> </exception>
					///	 
					public:
						virtual void read(std::string filePathName) throw(Exception);

						static void main(std::string args[]) throw(Exception);
					};

				}
			}
		}
	}
}